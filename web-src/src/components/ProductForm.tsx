import React, { useState, useEffect } from 'react'
import { Form } from 'react-bootstrap'
import { Column } from './SearchBar'

export interface ProductFormData {
    name: string
    description: string
    groupId: number
    manufacturer: string
    amount: number
    price: number
}

interface ProductFormProps {
    groups: { id: number; name: string }[]   // список групп для селекта
    onChange: (data: ProductFormData) => void
}

export default function ProductForm({ groups, onChange }: ProductFormProps) {
    const [data, setData] = useState<ProductFormData>({
        name: '',
        description: '',
        groupId: groups[0]?.id ?? 0,
        manufacturer: '',
        amount: 0,
        price: 0,
    })

    useEffect(() => {
        onChange(data)
    }, [data, onChange])

    return (
        <Form>
            <Form.Group className="mb-2">
                <Form.Label>Назва</Form.Label>
                <Form.Control
                    value={data.name}
                    onChange={e => setData(d => ({ ...d, name: e.target.value }))}
                />
            </Form.Group>
            <Form.Group className="mb-2">
                <Form.Label>Опис</Form.Label>
                <Form.Control
                    as="textarea"
                    rows={2}
                    value={data.description}
                    onChange={e => setData(d => ({ ...d, description: e.target.value }))}
                />
            </Form.Group>
            <Form.Group className="mb-2">
                <Form.Label>Група</Form.Label>
                <Form.Select
                    value={data.groupId}
                    onChange={e => setData(d => ({ ...d, groupId: Number(e.target.value) }))}
                >
                    {groups.map(g => (
                        <option key={g.id} value={g.id}>
                            {g.name}
                        </option>
                    ))}
                </Form.Select>
            </Form.Group>
            <Form.Group className="mb-2">
                <Form.Label>Виробник</Form.Label>
                <Form.Control
                    value={data.manufacturer}
                    onChange={e => setData(d => ({ ...d, manufacturer: e.target.value }))}
                />
            </Form.Group>
            <Form.Group className="mb-2">
                <Form.Label>К-сть</Form.Label>
                <Form.Control
                    type="number"
                    value={data.amount}
                    onChange={e => setData(d => ({ ...d, amount: +e.target.value }))}
                />
            </Form.Group>
            <Form.Group className="mb-2">
                <Form.Label>Ціна/од.</Form.Label>
                <Form.Control
                    type="number"
                    step="0.01"
                    value={data.price}
                    onChange={e => setData(d => ({ ...d, price: +e.target.value }))}
                />
            </Form.Group>
        </Form>
    )
}