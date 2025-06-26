// src/components/StockModal.tsx
import React, { useState } from 'react'
import { Modal, Button, Form } from 'react-bootstrap'

export interface StockItem {
    id: any
    label: string
}

export interface StockModalProps {
    show: boolean
    title: string
    items: StockItem[]
    selectedId?: any
    onConfirm: (id: any, amount: number) => void
    onCancel: () => void
    confirmLabel?: string
    cancelLabel?: string
}

const StockModal: React.FC<StockModalProps> = ({
                                                   show,
                                                   title,
                                                   items,
                                                   selectedId,
                                                   onConfirm,
                                                   onCancel,
                                                   confirmLabel = 'ОК',
                                                   cancelLabel = 'Відміна',
                                               }) => {
    const [currentId, setCurrentId] = useState<any>(selectedId ?? '')
    const [amount, setAmount] = useState<number>(0)

    const handleConfirm = () => {
        onConfirm(currentId, amount)
        setAmount(0)
    }

    return (
        <Modal show={show} onHide={onCancel}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-2">
                        <Form.Label>Виберіть</Form.Label>
                        <Form.Select
                            value={currentId}
                            onChange={(e) => setCurrentId(e.target.value)}
                        >
                            <option value="">—</option>
                            {items.map((it) => (
                                <option key={it.id} value={it.id}>
                                    {it.label}
                                </option>
                            ))}
                        </Form.Select>
                    </Form.Group>
                    <Form.Group className="mb-2">
                        <Form.Label>Кількість</Form.Label>
                        <Form.Control
                            type="number"
                            value={amount}
                            onChange={(e) => setAmount(Number(e.target.value))}
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="primary" onClick={handleConfirm} disabled={!currentId}>
                    {confirmLabel}
                </Button>
                <Button variant="secondary" onClick={onCancel}>
                    {cancelLabel}
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default StockModal
