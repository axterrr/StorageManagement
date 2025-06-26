import React from 'react'
import { Modal, Button } from 'react-bootstrap'

export interface ModalFormProps {
    show: boolean
    title: string
    children: React.ReactNode
    confirmLabel?: string
    cancelLabel?: string
    onConfirm: () => void
    onCancel: () => void
}

const ModalForm: React.FC<ModalFormProps> = ({
                                                 show,
                                                 title,
                                                 children,
                                                 confirmLabel = 'ОК',
                                                 cancelLabel = 'Відміна',
                                                 onConfirm,
                                                 onCancel,
                                             }) => (
    <Modal show={show} onHide={onCancel}>
        <Modal.Header closeButton>
            <Modal.Title>{title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>{children}</Modal.Body>
        <Modal.Footer>
            <Button variant="primary" onClick={onConfirm}>
                {confirmLabel}
            </Button>
            <Button variant="secondary" onClick={onCancel}>
                {cancelLabel}
            </Button>
        </Modal.Footer>
    </Modal>
)

export default ModalForm
