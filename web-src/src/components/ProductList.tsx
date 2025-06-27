import React, { useState, useEffect } from 'react';
import api from '../services/api';

type Product = {
    id: number;
    name: string;
};

const ProductsList: React.FC = () => {
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        setLoading(true);
        api
            .get<Product[]>('/products')
            .then(response => {
                setProducts(response.data);
            })
            .catch(err => {
                console.error('Ошибка при загрузке продуктов:', err);
                setError('Не удалось загрузить список продуктов');
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    if (loading) return <div>Загрузка...</div>;
    if (error)   return <div>{error}</div>;

    return (
        <div>
            <h2>Список продуктов</h2>
            <ul>
                {products.map(p => (
                    <li key={p.id}>{p.name}</li>
                ))}
            </ul>
        </div>
    );
};

export default ProductsList;
