// src/App.tsx
import React, { useState, useEffect } from 'react'
import api from './services/api.js'
import { login as doLogin } from './services/auth.js'
import LoginForm from './components/LoginForm.tsx'
import {
    Sidebar,
    SearchBar,
    DataTable,
    ConsoleLog,
    ModalForm,
    ConfirmModal,
    StockModal,
} from './components'
import './index.css'
import './App.css'

type Mode = 'product' | 'group'

const App: React.FC = () => {
    // Храним токен только в памяти
    const [token, setToken] = useState<string | null>(null)
    const isAuthed = token !== null

    // При смене токена — подставляем или очищаем заголовок
    useEffect(() => {
        if (token) {
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`
        } else {
            delete api.defaults.headers.common['Authorization']
        }
    }, [token])

    // Другие состояния
    const [mode, setMode] = useState<Mode>('product')
    const [search, setSearch] = useState('')
    const [logs, setLogs] = useState<string[]>([])
    const [showAdd, setShowAdd] = useState(false)
    const [showDelete, setShowDelete] = useState(false)
    const [showStock, setShowStock] = useState<null | 'in' | 'out'>(null)
    const [rows, setRows] = useState<any[]>([])
    const [selected, setSelected] = useState<any | null>(null)

    // После авторизации и при смене режима загружаем данные
    useEffect(() => {
        if (!isAuthed) return
        const endpoint = mode === 'product' ? '/products' : '/groups'
        api
            .get(endpoint)
            .then(res => setRows(res.data))
            .catch(err => setLogs(l => [...l, `Error loading ${endpoint}: ${err.message}`]))
    }, [mode, isAuthed])

    if (!isAuthed) {
        return (
            <LoginForm
                onSubmit={async (username, password) => {
                    console.log("submitted")
                    const t = await doLogin(username, password);
                    setToken(t)
                }}
            />
        )
    }

    const columns = mode === 'product'
        ? [
            { header: 'ID', accessor: 'id' },
            { header: 'Назва', accessor: 'name' },
            { header: 'Опис', accessor: 'description' },
            { header: 'Група', accessor: 'group' },
            { header: 'Виробник', accessor: 'manufacturer' },
            { header: 'К-сть', accessor: 'quantity' },
            { header: 'Ціна/од.', accessor: 'unitPrice' },
        ]
        : [
            { header: 'ID', accessor: 'id' },
            { header: 'Назва групи', accessor: 'name' },
            { header: 'Опис', accessor: 'description' },
        ]

    return (
        <div className="app-container">
            <div className="main-row">
                {/* Sidebar */}
                <div className="sidebar-wrapper">
                    <Sidebar
                        mode={mode}
                        onToggle={() => setMode(prev => prev === 'product' ? 'group' : 'product')}
                        onAdd={() => setShowAdd(true)}
                        onDelete={() => setShowDelete(true)}
                        onStockIn={() => setShowStock('in')}
                        onStockOut={() => setShowStock('out')}
                        onStats={() => {}}
                    />
                </div>

                {/* Основной контент */}
                <div className="content-wrapper">
                    <div className="search-wrapper">
                        <SearchBar
                            value={search}
                            onChange={setSearch}
                            onClear={() => setSearch('')}
                        />
                    </div>

                    <div className="data-table-wrapper">
                        <DataTable
                            columns={columns}
                            rows={rows.filter(row =>
                                Object.values(row)
                                    .join(' ')
                                    .toLowerCase()
                                    .includes(search.toLowerCase())
                            )}
                            onRowClick={row => {
                                setSelected(row)
                                setLogs(prev => [...prev, `Вибрано: ${row.id}`])
                            }}
                        />
                        {/* Отладочный вывод */}
                        <pre>{JSON.stringify(rows, null, 2)}</pre>
                    </div>

                    <div className="console-wrapper">
                        <ConsoleLog logs={logs} />
                    </div>
                </div>

                {/* Плейсхолдер для модалок */}
                <div className="modals-wrapper" />
            </div>

            {/* Модалка добавления */}
            <ModalForm
                show={showAdd}
                title={mode === 'product' ? 'Додати товар' : 'Додати групу'}
                onConfirm={() => {
                    // TODO: api.post или api.put, затем обновить rows
                    setShowAdd(false)
                }}
                onCancel={() => setShowAdd(false)}
            >
                {/* TODO: поля формы */}
            </ModalForm>

            {/* Подтверждение удаления */}
            <ConfirmModal
                show={showDelete}
                message={`Видалити обраний ${mode === 'product' ? 'товар' : 'групу'}?`}
                onConfirm={() => {
                    if (selected) {
                        const path = mode === 'product' ? '/products' : '/groups'
                        api.delete(`${path}/${selected.id}`)
                            .then(() => setRows(r => r.filter(x => x.id !== selected.id)))
                    }
                    setShowDelete(false)
                }}
                onCancel={() => setShowDelete(false)}
            />

            {/* Модалка приёма/списания */}
            {showStock && selected && (
                <StockModal
                    show
                    title={showStock === 'in' ? 'Приймання на склад' : 'Списання зі складу'}
                    items={[selected]}
                    onConfirm={qty => {
                        const action = showStock === 'in' ? 'increase-amount' : 'decrease-amount'
                        api.post(`/products/${selected.id}/${action}`, { amount: qty })
                            .then(() => {
                                // перезагрузить данные
                                const e = mode === 'product' ? '/products' : '/groups'
                                return api.get(e)
                            })
                            .then(res => setRows(res.data))
                    }}
                    onCancel={() => setShowStock(null)}
                />
            )}
        </div>
    )
}

export default App
