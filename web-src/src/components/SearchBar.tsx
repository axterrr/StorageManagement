// src/components/SearchBar.tsx
import React from 'react'
import { Form, InputGroup, Button } from 'react-bootstrap'

export interface SearchBarProps {
    value: string
    onChange: (value: string) => void
    onClear?: () => void
}

const SearchBar: React.FC<SearchBarProps> = ({ value, onChange, onClear }) => (
    <InputGroup className="mb-2">
        <Form.Control
            placeholder="Пошук..."
            value={value}
            onChange={e => onChange(e.target.value)}
        />
        {onClear && value && (
            <Button variant="outline-secondary" onClick={onClear}>
                Очистити
            </Button>
        )}
    </InputGroup>
)

export default SearchBar
