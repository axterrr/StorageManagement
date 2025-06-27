import api from './api.js';

export async function login(username, password) {
    const { data } = await api.post(
        '/auth/login',
        { username, password },
        { headers: { 'Content-Type': 'application/json' } }
    );
    return data.token;
}