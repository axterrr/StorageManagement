// src/components/DataTable.tsx
import React from 'react'
import { Table } from 'react-bootstrap'

export interface Column {
    header: string
    accessor: string
}

export interface DataTableProps {
    columns: Column[]
    rows: Record<string, any>[]
    onRowClick?: (row: Record<string, any>) => void
    selectedRowId?: any
}

const DataTable: React.FC<DataTableProps> = ({
                                                 columns,
                                                 rows,
                                                 onRowClick,
                                                 selectedRowId,
                                             }) => (
    <Table striped bordered hover size="sm">
        <thead>
        <tr>
            {columns.map(col => (
                <th key={col.accessor}>{col.header}</th>
            ))}
        </tr>
        </thead>
        <tbody>
        {rows.map((row, idx) => {
            const isSelected =
                selectedRowId !== undefined && row.id === selectedRowId
            return (
                <tr
                    key={row.id ?? idx}
                    onClick={() => onRowClick?.(row)}
                    className={isSelected ? 'table-active' : undefined}
                    style={{ cursor: onRowClick ? 'pointer' : undefined }}
                >
                    {columns.map(col => (
                        <td key={col.accessor}>{row[col.accessor]}</td>
                    ))}
                </tr>
            )
        })}
        </tbody>
    </Table>
)

export default DataTable
