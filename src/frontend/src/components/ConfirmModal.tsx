import React from 'react'
import { Modal, Button } from 'react-bootstrap'

export interface ConfirmModalProps {
    show: boolean
    message: string
    onConfirm: () => void
    onCancel: () => void
    confirmLabel?: string
    cancelLabel?: string
}

const ConfirmModal: React.FC<ConfirmModalProps> = ({
                                                       show,
                                                       message,
                                                       onConfirm,
                                                       onCancel,
                                                       confirmLabel = 'ОК',
                                                       cancelLabel = 'Відміна',
                                                   }) => (
    <Modal show={show} onHide={onCancel}>
        <Modal.Header closeButton>
            <Modal.Title>Підтвердіть дію</Modal.Title>
        </Modal.Header>
        <Modal.Body>{message}</Modal.Body>
        <Modal.Footer>
            <Button variant="danger" onClick={onConfirm}>
                {confirmLabel}
            </Button>
            <Button variant="secondary" onClick={onCancel}>
                {cancelLabel}
            </Button>
        </Modal.Footer>
    </Modal>
)

export default ConfirmModal
