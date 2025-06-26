// src/App.tsx
import React, { useState } from 'react'
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

const App: React.FC = () => {
    const [mode, setMode] = useState<'product' | 'group'>('product')
    const [search, setSearch] = useState('')
    const [logs, setLogs] = useState<string[]>([])
    const [showAdd, setShowAdd] = useState(false)
    const [showDelete, setShowDelete] = useState(false)
    const [showStock, setShowStock] = useState<null | 'in' | 'out'>(null)

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

    const rows: any[] = [] // замінити на дані з API

    return (
        <div className="app-container">
            <div className="main-row">
                {/* Sidebar */}
                <div className="sidebar-wrapper">
                    <Sidebar
                        mode={mode}
                        onToggle={() =>
                            setMode((prev) => (prev === 'product' ? 'group' : 'product'))
                        }
                        onAdd={() => setShowAdd(true)}
                        onDelete={() => setShowDelete(true)}
                        onStockIn={() => setShowStock('in')}
                        onStockOut={() => setShowStock('out')}
                        onStats={() => {}}
                    />
                </div>

                {/* Основний контент */}
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
                            rows={rows.filter((row) =>
                                Object.values(row)
                                    .join(' ')
                                    .toLowerCase()
                                    .includes(search.toLowerCase())
                            )}
                            onRowClick={(row) =>
                                setLogs((prev) => [...prev, `Вибрано: ${row.id}`])
                            }
                        />
                    </div>

                    <div className="console-wrapper">
                        <ConsoleLog logs={logs} />
                    </div>
                </div>

                {/* Права колонка під модалки */}
                <div className="modals-wrapper" />
            </div>

            {/* Модалки */}
            <ModalForm
                show={showAdd}
                title={mode === 'product' ? 'Додати товар' : 'Додати групу'}
                onConfirm={() => setShowAdd(false)}
                onCancel={() => setShowAdd(false)}
            >
                {/* Поля форми */}
            </ModalForm>

            <ConfirmModal
                show={showDelete}
                message={`Видалити обраний ${
                    mode === 'product' ? 'товар' : 'групу'
                }?`}
                onConfirm={() => setShowDelete(false)}
                onCancel={() => setShowDelete(false)}
            />

            {showStock && (
                <StockModal
                    show
                    title={
                        showStock === 'in' ? 'Приймання на склад' : 'Списання зі складу'
                    }
                    items={[]} /* список передамо пізніше */
                    onConfirm={() => setShowStock(null)}
                    onCancel={() => setShowStock(null)}
                />
            )}
        </div>
    )
}

export default App