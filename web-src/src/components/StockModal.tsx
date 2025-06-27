// src/components/StockModal.tsx
import React, { useState, useEffect } from 'react'
import { Modal, Button, Form } from 'react-bootstrap'

export interface StockItem {
    id: number
    name: string
    // любые другие поля, которые вам нужны
}

export interface StockModalProps {
    show: boolean
    title: string
    items: StockItem[]
    /**
     * Вызывается при подтверждении.
     * Первый аргумент — выбранный item,
     * второй — введённая quantity.
     */
    onConfirm: (item: StockItem, quantity: number) => void
    onCancel: () => void
}

const StockModal: React.FC<StockModalProps> = ({
                                                   show,
                                                   title,
                                                   items,
                                                   onConfirm,
                                                   onCancel,
                                               }) => {
    const [selectedId, setSelectedId] = useState<number>(items[0]?.id ?? 0)
    const [quantity, setQuantity]   = useState<number>(0)

    // Если список items меняется, по умолчанию выбираем первый
    useEffect(() => {
        if (items.length > 0) {
            setSelectedId(items[0].id)
        }
    }, [items])

    const handleOk = () => {
        const item = items.find(i => i.id === selectedId)
        if (item) {
            onConfirm(item, quantity)
        }
    }

    return (
        <Modal show={show} onHide={onCancel}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form.Group className="mb-3">
                    <Form.Label>Виберіть</Form.Label>
                    <Form.Select
                        value={selectedId}
                        onChange={e => setSelectedId(Number(e.target.value))}
                    >
                        {items.map(i => (
                            <option key={i.id} value={i.id}>
                                {i.name}
                            </option>
                        ))}
                    </Form.Select>
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Кількість</Form.Label>
                    <Form.Control
                        type="number"
                        value={quantity}
                        onChange={e => setQuantity(Number(e.target.value))}
                    />
                </Form.Group>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onCancel}>
                    Відміна
                </Button>
                <Button variant="primary" onClick={handleOk}>
                    OK
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default StockModal