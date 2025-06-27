import React, { useState, useEffect } from 'react'
import { Form } from 'react-bootstrap'

export interface GroupFormData {
    name: string
    description: string
}

interface GroupFormProps {
    onChange: (data: GroupFormData) => void
}

export default function GroupForm({ onChange }: GroupFormProps) {
    const [data, setData] = useState<GroupFormData>({ name: '', description: ''})

    useEffect(() => {
        onChange(data)
    }, [data, onChange])

    return (
        <Form>
            <Form.Group className="mb-2">
                <Form.Label>Назва групи</Form.Label>
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
        </Form>
    )
}
