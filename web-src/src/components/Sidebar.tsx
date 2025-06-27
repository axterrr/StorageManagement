// src/components/Sidebar.tsx
import React from 'react'
import { Button } from 'react-bootstrap'

export interface SidebarProps {
    mode: 'product' | 'group'
    onToggle: () => void
    onAdd: () => void
    onDelete: () => void
    onStockIn: () => void
    onStockOut: () => void
    onStats: () => void
    /** Новый проп: можно ли удалять */
    canDelete?: boolean
}

const Sidebar: React.FC<SidebarProps> = ({
                                             mode,
                                             onToggle,
                                             onAdd,
                                             onDelete,
                                             onStockIn,
                                             onStockOut,
                                             onStats,
                                             canDelete = false,       // по умолчанию – нельзя
                                         }) => (
    <div className="sidebar p-3">
        <Button
            variant="outline-primary"
            onClick={onToggle}
            style={{ width: '100%', marginBottom: 8 }}
        >
            {mode === 'product' ? 'Показати групи' : 'Показати товари'}
        </Button>

        <Button
            variant="primary"
            onClick={onAdd}
            style={{ width: '100%', marginBottom: 8 }}
        >
            Додати
        </Button>
        <Button
            variant="danger"
            onClick={onDelete}
            disabled={!canDelete}
            style={{ width: '100%', marginBottom: 8 }}
        >
            Видалити
        </Button>

        <hr />

        <Button
            variant="outline-success"
            onClick={onStockIn}
            style={{ width: '100%', marginBottom: 8 }}
        >
            Прийом на склад
        </Button>
        <Button
            variant="outline-secondary"
            onClick={onStockOut}
            style={{ width: '100%', marginBottom: 8 }}
        >
            Списання зі складу
        </Button>
        <Button
            variant="success"
            onClick={onStats}
            style={{ width: '100%' }}
        >
            Статистика
        </Button>
    </div>
)

export default Sidebar
