import React, { useState } from 'react'
import 'bootstrap/dist/css/bootstrap.min.css'
import './index.css'
import { Sidebar, SearchBar, DataTable, ConsoleLog, ConfirmModal, ModalForm, StockModal } from './components'

const App: React.FC = () => {
    const [mode, setMode] = useState<'product' | 'group'>('product')
    const [search, setSearch] = useState('')
    const [logs, setLogs] = useState<string[]>([])
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
    const rows: any[] = []

    const [showAdd, setShowAdd] = useState(false)
    const [showDelete, setShowDelete] = useState(false)
    const [showStock, setShowStock] = useState<null | 'in' | 'out'>(null)
    const [showStats, setShowStats] = useState(false)

    return (
        <div className="h-100 d-flex flex-column">
            <div className="flex-grow-1">
                <div className="container-fluid p-3">
                    <div className="row">
                        <div className="col-2">
                            <Sidebar
                                mode={mode}
                                onToggle={() => setMode(mode === 'product' ? 'group' : 'product')}
                                onAdd={() => setShowAdd(true)}
                                onDelete={() => setShowDelete(true)}
                                onStockIn={() => setShowStock('in')}
                                onStockOut={() => setShowStock('out')}
                                onStats={() => setShowStats(true)}
                            />
                        </div>

                        <div className="col-7">
                            <SearchBar
                                value={search}
                                onChange={setSearch}
                                onClear={() => setSearch('')}
                            />

                            <DataTable
                                columns={columns}
                                rows={rows.filter(row =>
                                    Object.values(row).some(val =>
                                        String(val).toLowerCase().includes(search.toLowerCase())
                                    )
                                )}
                            />

                            <ConsoleLog logs={logs} />
                        </div>

                        <div className="col-3">
                        </div>
                    </div>
                </div>
            </div>

            <ModalForm
                show={showAdd}
                title={mode === 'product' ? 'Додати товар' : 'Додати групу'}
                onConfirm={() => {}}
                onCancel={() => setShowAdd(false)}
            >
            </ModalForm>

            <ConfirmModal
                show={showDelete}
                message={`Видалити обраний ${mode === 'product' ? 'товар' : 'групу'}?`}
                onConfirm={() => {}}
                onCancel={() => setShowDelete(false)}
            />

            {showStock && (
                <StockModal
                    show={true}
                    title={showStock === 'in' ? 'Приймання на склад' : 'Списання зі складу'}
                    items={[]}
                    onConfirm={() => {}}
                    onCancel={() => setShowStock(null)}
                />
            )}

        </div>
    )
}

export default App