import React from 'react'
import { InputGroup, FormControl, Dropdown, Button } from 'react-bootstrap'

export interface Column {
    header: string
    accessor: string
}

export interface SearchBarProps {
    value: string
    onChange: (v: string) => void
    onClear: () => void

    columns: Column[]
    selected?: string
    onSelectColumn: (accessor?: string) => void
}

export default function SearchBar({
                                      value,
                                      onChange,
                                      onClear,
                                      columns,
                                      selected,
                                      onSelectColumn,
                                  }: SearchBarProps) {
    return (
        <InputGroup className="mb-3">
            <FormControl
                placeholder="Пошук…"
                value={value}
                onChange={e => onChange(e.target.value)}
            />
            <Button variant="outline-secondary" onClick={onClear}>
                ✕
            </Button>

            <Dropdown align="end">
                <Dropdown.Toggle variant="outline-primary">
                    {selected
                        ? columns.find(c => c.accessor === selected)?.header
                        : 'Фільтри'}
                </Dropdown.Toggle>
                <Dropdown.Menu>
                    <Dropdown.Item
                        active={!selected}
                        onClick={() => onSelectColumn(undefined)}
                    >
                        Усі поля
                    </Dropdown.Item>
                    {columns.map(col => (
                        <Dropdown.Item
                            key={col.accessor}
                            active={col.accessor === selected}
                            onClick={() => onSelectColumn(col.accessor)}
                        >
                            {col.header}
                        </Dropdown.Item>
                    ))}
                </Dropdown.Menu>
            </Dropdown>
        </InputGroup>
    )
}