import React, { useRef, useState } from 'react'

interface ConsoleLogProps {
    logs: string[]
    initialHeight?: number
    minHeight?: number
    maxHeight?: number
}

const ConsoleLog: React.FC<ConsoleLogProps> = ({
                                                   logs,
                                                   initialHeight = 150,
                                                   minHeight = 50,
                                                   maxHeight = 600,
                                               }) => {
    const [height, setHeight] = useState<number>(initialHeight)
    const startYRef = useRef<number>(0)
    const startHeightRef = useRef<number>(initialHeight)

    const onMouseDown = (e: React.MouseEvent) => {
        startYRef.current = e.clientY
        startHeightRef.current = height

        const handleMouseMove = (event: MouseEvent) => {
            const deltaY = event.clientY - startYRef.current
            let newHeight = startHeightRef.current - deltaY
            newHeight = Math.max(minHeight, Math.min(maxHeight, newHeight))
            setHeight(newHeight)
        }
        const handleMouseUp = () => {
            document.removeEventListener('mousemove', handleMouseMove)
            document.removeEventListener('mouseup', handleMouseUp)
        }

        document.addEventListener('mousemove', handleMouseMove)
        document.addEventListener('mouseup', handleMouseUp)
    }

    return (
        <div
            className="border bg-light"
            style={{
                position: 'relative',
                height: `${height}px`,
                overflowY: 'auto',
                fontFamily: 'monospace',
            }}
        >
            <div
                onMouseDown={onMouseDown}
                style={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    right: 0,
                    height: '8px',
                    cursor: 'ns-resize',
                    zIndex: 10,
                    background: 'transparent',
                }}
            />
            <div style={{ paddingTop: '8px' }}>
                {logs.map((line, idx) => (
                    <div key={idx}>{line}</div>
                ))}
            </div>
        </div>
    )
}

export default ConsoleLog
