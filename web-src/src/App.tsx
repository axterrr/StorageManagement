// src/App.tsx
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
    // --- Auth & token header ---
    const [token, setToken] = useState<string | null>(null)
    const isAuthed = token !== null
    useEffect(() => {
        if (token) api.defaults.headers.common['Authorization'] = `Bearer ${token}`
        else delete api.defaults.headers.common['Authorization']
    }, [token])

    // --- UI state ---
    const [mode, setMode] = useState<Mode>('product')
    const [search, setSearch] = useState<string>('')
    const [filterColumn, setFilterColumn] = useState<string | undefined>(undefined)
    const [logs, setLogs] = useState<string[]>([])
    const [showAdd, setShowAdd] = useState<boolean>(false)
    const [showDelete, setShowDelete] = useState<boolean>(false)
    const [showStock, setShowStock] = useState<null | 'in' | 'out'>(null)
    const [rows, setRows] = useState<any[]>([])
    const [selected, setSelected] = useState<any | null>(null)
    const [sortConfig, setSortConfig] = useState<SortConfig | null>(null)

    // --- Load data on auth or mode change ---
    useEffect(() => {
        if (!isAuthed) return
        setSortConfig(null)
        setFilterColumn(undefined)
        const endpoint = mode === 'product' ? '/product' : '/group'
        api
            .get(endpoint)
            .then(res => setRows(res.data))
            .catch(err => setLogs(l => [...l, `Error loading ${endpoint}: ${err.message}`]))
    }, [mode, isAuthed])

    // --- Sorting handler ---
    const handleSort = (col: { accessor: string | ((row: any) => any) }) => {
        if (typeof col.accessor !== 'string') return
        const key = col.accessor
        let direction: 'asc' | 'desc' = 'asc'
        if (sortConfig?.accessor === key && sortConfig.direction === 'asc') {
            direction = 'desc'
        }
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

    // --- Action handlers ---
    const handleAdd = (data: any) => {
        const endpoint = mode === 'product' ? '/product' : '/group'
        api
            .post(endpoint, data)
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
        api
            .delete(`${endpoint}/${selected.id}`)
            .then(() => setRows(r => r.filter(x => x.id !== selected.id)))
            .catch(err => setLogs(l => [...l, `Error deleting ${mode}: ${err.message}`]))
            .finally(() => setShowDelete(false))
    }

    const handleStock = (item: { id: number }, qty: number) => {
        if (!item) {
            setShowStock(null)
            return
        }
        const action = showStock === 'in' ? 'increase-amount' : 'decrease-amount'
        setLogs(l => [
            ...l,
            `Stock ${action.replace('-amount','')}: item id: ${item.id}, amount: ${qty}`
        ])
        api
            .post(`/product/${item.id}/${action}`, { amount: qty })
            .then(() => api.get('/product'))
            .then(res => setRows(res.data))
            .catch(err => setLogs(l => [...l, `Error stock ${mode}: ${err.message}`]))
            .finally(() => setShowStock(null))
    }

    // --- Columns definition ---
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

    // --- Filtering rows ---
    const filteredRows = rows.filter(row => {
        const q = search.trim().toLowerCase()
        if (!q) return true
        if (filterColumn) {
            const cell = String((row as any)[filterColumn] ?? '').toLowerCase()
            return cell.includes(q)
        }
        return Object.values(row)
            .join(' ')
            .toLowerCase()
            .includes(q)
    })

    // --- Render login or main UI ---
    if (!isAuthed) {
        return <LoginForm onSubmit={async (u, p) => setToken(await doLogin(u, p))} />
    }

    return (
        <div className="app-container">
            <div className="main-row">
                {/* Sidebar + Logs */}
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
                    <div className="sidebar-logs">
                        <ConsoleLog logs={logs} />
                    </div>
                </div>

                {/* Main content */}
                <div className="content-wrapper">
                    <div className="search-wrapper">
                        <SearchBar
                            value={search}
                            onChange={setSearch}
                            onClear={() => setSearch('')}
                            columns={columns}
                            selected={filterColumn}
                            onSelectColumn={setFilterColumn}
                        />
                    </div>
                    <div className="data-table-wrapper">
                        <DataTable
                            columns={columns}
                            rows={filteredRows}
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

            {/* Modals */}
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

            <StockModal
                show={showStock !== null}
                title={showStock === 'in' ? 'Приймання на склад' : 'Списання зі складу'}
                items={rows}
                onConfirm={handleStock}
                onCancel={() => setShowStock(null)}
            />
        </div>
    )
}

export default App