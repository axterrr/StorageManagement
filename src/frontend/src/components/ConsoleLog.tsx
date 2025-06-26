import React from 'react'

interface ConsoleLogProps {
    logs: string[]
    height?: string
}

const ConsoleLog: React.FC<ConsoleLogProps> = ({
                                                   logs,
                                                   height = '150px',
                                               }) => (
    <div
        className="border p-2 bg-light"
        style={{ height, overflowY: 'auto', fontFamily: 'monospace' }}
    >
        {logs.map((line, idx) => (
            <div key={idx}>{line}</div>
        ))}
    </div>
)

export default ConsoleLog
