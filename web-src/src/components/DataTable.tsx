// src/components/DataTable.tsx
import React, { ReactNode } from 'react'
import { Table } from 'react-bootstrap'

export interface Column<T> {
    header: string
    accessor: keyof T | ((row: T) => ReactNode)
    width?: number | string
}

export interface DataTableProps<T extends { id?: string | number }> {
    columns: Column<T>[]
    rows: T[]
    onRowClick?: (row: T) => void
    selectedRowId?: T['id']
    onSort?: (col: Column<T>) => void
}

function DataTable<T extends { id?: string | number }>({
                                                           columns,
                                                           rows,
                                                           onRowClick,
                                                           selectedRowId,
                                                           onSort,
                                                       }: DataTableProps<T>) {
    return (
        <div style={{ width: '100%', overflowX: 'auto' }}>
            <Table striped bordered hover size="sm">
                <thead>
                <tr>
                    {columns.map((col, i) => (
                        <th
                            key={i}
                            style={{ cursor: onSort ? 'pointer' : undefined, width: col.width }}
                            onClick={() => onSort?.(col)}
                        >
                            {col.header}
                        </th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {rows.map((row, ri) => {
                    const isSelected =
                        selectedRowId !== undefined && row.id === selectedRowId
                    return (
                        <tr
                            key={row.id ?? ri}
                            onClick={() => onRowClick?.(row)}
                            className={isSelected ? 'table-active' : undefined}
                            style={{ cursor: onRowClick ? 'pointer' : undefined }}
                        >
                            {columns.map((col, ci) => {
                                const value =
                                    typeof col.accessor === 'function'
                                        ? col.accessor(row)
                                        : (row as any)[col.accessor]
                                return (
                                    <td key={ci} style={col.width ? { width: col.width } : undefined}>
                                        {value}
                                    </td>
                                )
                            })}
                        </tr>
                    )
                })}
                </tbody>
            </Table>
        </div>
    )
}

export default DataTable
