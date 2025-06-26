import React from 'react';
import { Button, Form } from 'react-bootstrap';

interface SidebarProps {
    mode: 'product' | 'group';
    onToggle: () => void;
    onAdd: () => void;
    onDelete: () => void;
    onStockIn: () => void;
    onStockOut: () => void;
    onStats: () => void;
}

const Sidebar: React.FC<SidebarProps> = ({
                                             mode,
                                             onToggle,
                                             onAdd,
                                             onDelete,
                                             onStockIn,
                                             onStockOut,
                                             onStats
                                         }) => (
    <div className="d-flex flex-column h-100">
        <Form.Check
            type="switch"
            id="toggle-mode"
            label={
                mode === 'product'
                    ? 'Показати: Товари ↔ Групи'
                    : 'Показати: Групи ↔ Товари'
            }
            onChange={onToggle}
            className="mb-3"
        />
        <Button onClick={onAdd} className="mb-2">
            Додати
        </Button>
        <Button onClick={onDelete} className="mb-2">
            Видалити
        </Button>
        <div className="mt-auto">
            <Button
                variant="outline-primary"
                onClick={onStockIn}
                className="mb-2"
            >
                Прийом на склад
            </Button>
            <Button
                variant="outline-secondary"
                onClick={onStockOut}
                className="mb-2"
            >
                Списання зі складу
            </Button>
            <Button variant="outline-success" onClick={onStats}>
                Статистика
            </Button>
        </div>
    </div>
);

export default Sidebar;
