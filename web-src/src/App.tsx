import React, { useState, useEffect } from 'react'
import api from './services/api.js'
import { login as doLogin } from './services/auth.js'
import LoginForm from './components/LoginForm'
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
type SortConfig = { accessor: string; direction: 'asc' | 'desc' }

const App: React.FC = () => {
    const [token, setToken] = useState<string | null>(null)
    const isAuthed = token !== null
    useEffect(() => {
        if (token) api.defaults.headers.common['Authorization'] = `Bearer ${token}`
        else delete api.defaults.headers.common['Authorization']
    }, [token])

    const [mode, setMode] = useState<Mode>('product')
    const [search, setSearch] = useState<string>('')
    const [logs, setLogs] = useState<string[]>([])
    const [showAdd, setShowAdd] = useState<boolean>(false)
    const [showDelete, setShowDelete] = useState<boolean>(false)
    const [showStock, setShowStock] = useState<null | 'in' | 'out'>(null)
    const [rows, setRows] = useState<any[]>([])
    const [selected, setSelected] = useState<any | null>(null)
    const [sortConfig, setSortConfig] = useState<SortConfig | null>(null)

    useEffect(() => {
        if (!isAuthed) return
        setSortConfig(null)
        const endpoint = mode === 'product' ? '/product' : '/group'
        api.get(endpoint)
            .then(res => setRows(res.data))
            .catch(err => setLogs(l => [...l, `Error loading ${endpoint}: ${err.message}`]))
    }, [mode, isAuthed])

    const handleSort = (col: { accessor: string | ((r:any)=>any) }) => {
        if (typeof col.accessor !== 'string') return
        const key = col.accessor
        let direction: 'asc' | 'desc' = 'asc'
        if (sortConfig?.accessor === key && sortConfig.direction === 'asc') direction = 'desc'
        setSortConfig({ accessor: key, direction })
        const sorted = [...rows].sort((a, b) => {
            const va = a[key], vb = b[key]
            if (va == null) return 1
            if (vb == null) return -1
            if (va < vb) return direction === 'asc' ? -1 : 1
            if (va > vb) return direction === 'asc' ? 1 : -1
            return 0
        })
        setRows(sorted)
    }

    const handleAdd = (data: any) => {
        const endpoint = mode === 'product' ? '/product' : '/group'
        api.post(endpoint, data)
            .then(res => {
                const newRow = mode === 'product' ? res.data : { id: res.data.id, ...data }
                setRows(r => [...r, newRow])
                setLogs(l => [...l, `${mode} created: ${res.data.id}`])
            })
            .catch(err => setLogs(l => [...l, `Error creating ${mode}: ${err.message}`]))
            .finally(() => setShowAdd(false))
    }

    const handleDelete = () => {
        if (!selected) return setShowDelete(false)
        const endpoint = mode === 'product' ? '/product' : '/group'
        api.delete(`${endpoint}/${selected.id}`)
            .then(() => setRows(r => r.filter(x => x.id !== selected.id)))
            .catch(err => setLogs(l => [...l, `Error deleting ${mode}: ${err.message}`]))
            .finally(() => setShowDelete(false))
    }

    const handleStock = (qty: number) => {
        if (!selected) return setShowStock(null)
        const action = showStock === 'in' ? 'increase-amount' : 'decrease-amount'
        api.post(`/product/${selected.id}/${action}`, { amount: qty })
            .then(() => api.get('/product'))
            .then(res => setRows(res.data))
            .catch(err => setLogs(l => [...l, `Error stock ${mode}: ${err.message}`]))
            .finally(() => setShowStock(null))
    }

    if (!isAuthed) {
        return <LoginForm onSubmit={async (u, p) => setToken(await doLogin(u, p))} />
    }

    const columns = mode === 'product'
        ? [
            { header: 'ID', accessor: 'id', width: 60 },
            { header: 'Назва', accessor: 'name', width: 200 },
            { header: 'Опис', accessor: 'description', width: 300 },
            { header: 'Група', accessor: 'groupId', width: 120 },
            { header: 'Виробник', accessor: 'manufacturer', width: 150 },
            { header: 'К-сть', accessor: 'amount', width: 80 },
            { header: 'Ціна/од.', accessor: 'price', width: 100 },
        ]
        : [
            { header: 'ID', accessor: 'id' },
            { header: 'Назва групи', accessor: 'name' },
            { header: 'Опис', accessor: 'description' },
        ]

    return (
        <div className="app-container">
            <div className="main-row">
                <div className="sidebar-wrapper">
                    <Sidebar
                        mode={mode}
                        onToggle={() => setMode(m => (m === 'product' ? 'group' : 'product'))}
                        onAdd={() => setShowAdd(true)}
                        onDelete={() => setShowDelete(true)}
                        onStockIn={() => setShowStock('in')}
                        onStockOut={() => setShowStock('out')}
                        onStats={() => {}}
                        canDelete={selected !== null}
                    />
                    {/* Логи под кнопками */}
                    <div className="sidebar-logs" style={{ marginTop: 16 }}>
                        <ConsoleLog logs={logs} />
                    </div>
                </div>

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
                            rows={rows.filter(r =>
                                Object.values(r)
                                    .join(' ')
                                    .toLowerCase()
                                    .includes(search.toLowerCase())
                            )}
                            onRowClick={row => {
                                setSelected(row)
                                setLogs(l => [...l, `Selected: ${row.id}`])
                            }}
                            selectedRowId={selected?.id}
                            onSort={handleSort}
                        />
                    </div>
                </div>
            </div>

            <ModalForm
                show={showAdd}
                title={mode === 'product' ? 'Додати товар' : 'Додати групу'}
                onConfirm={handleAdd}
                onCancel={() => setShowAdd(false)}
            />

            <ConfirmModal
                show={showDelete}
                message={`Видалити обраний ${mode === 'product' ? 'товар' : 'групу'}?`}
                onConfirm={handleDelete}
                onCancel={() => setShowDelete(false)}
            />

            {showStock && (
                <StockModal
                    show
                    title={showStock === 'in' ? 'Приймання на склад' : 'Списання зі складу'}
                    items={selected ? [selected] : []}
                    onConfirm={handleStock}
                    onCancel={() => setShowStock(null)}
                />
            )}
        </div>
    )
}

export default App
