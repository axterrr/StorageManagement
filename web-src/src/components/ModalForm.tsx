import React, { ReactNode, useState } from 'react'
import { Modal, Button, Form } from 'react-bootstrap'

export interface ModalFormProps<T = any> {
    show: boolean
    title: string
    onConfirm: (data: T) => void
    onCancel: () => void
    children?: ReactNode
}

export function ModalForm<T>({ show, title, onConfirm, onCancel, children }: ModalFormProps<T>) {
    const [formData, setFormData] = useState<T>({} as T)

    if (!show) return null

    return (
        <Modal show={show} onHide={onCancel}>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {/*
          Вам нужно в дочерних компонентах (children) вызывать
          setFormData({ ...formData, field: value })
        */}
                {children}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onCancel}>
                    Відміна
                </Button>
                <Button variant="primary" onClick={() => onConfirm(formData)}>
                    ОК
                </Button>
            </Modal.Footer>
        </Modal>
    )
}

export default ModalForm