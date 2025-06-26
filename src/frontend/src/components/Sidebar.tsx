// src/components/Sidebar.tsx
import React from 'react'
import { Button, Form } from 'react-bootstrap'

interface SidebarProps {
    mode: 'product' | 'group'
    onToggle: () => void
    onAdd: () => void
    onDelete: () => void
    onStockIn: () => void
    onStockOut: () => void
    onStats: () => void
}

const Sidebar: React.FC<SidebarProps> = ({
                                             mode,
                                             onToggle,
                                             onAdd,
                                             onDelete,
                                             onStockIn,
                                             onStockOut,
                                             onStats,
                                         }) => (
    <div className="d-flex flex-column h-100 p-3">
        {/* Toggle */}
        <div className="mb-3 w-100 text-center">
            <Form.Check
                type="switch"
                id="toggle-mode"
                className="mx-auto"
                onChange={onToggle}
            />
            <Form.Label className="mt-1">
                {mode === 'product'
                    ? 'Показати: Товари ⇄ Групи'
                    : 'Показати: Групи ⇄ Товари'}
            </Form.Label>
        </div>

        <hr className="w-100" />

        {/* CRUD Buttons */}
        <Button onClick={onAdd} className="mb-2 w-100">
            Додати
        </Button>
        <Button onClick={onDelete} className="mb-2 w-100">
            Видалити
        </Button>

        <div className="mt-auto">
            {/* Stock operations */}
            <Button
                variant="outline-primary"
                onClick={onStockIn}
                className="mb-2 w-100"
            >
                Прийом на склад
            </Button>
            <Button
                variant="outline-secondary"
                onClick={onStockOut}
                className="mb-2 w-100"
            >
                Списання зі складу
            </Button>

            <hr className="w-100 mt-3" />

            {/* Statistics */}
            <Button variant="outline-success" onClick={onStats} className="w-100">
                Статистика
            </Button>
        </div>
    </div>
)

export default Sidebar
