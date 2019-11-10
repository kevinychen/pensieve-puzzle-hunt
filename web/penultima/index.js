import "./style.css";
import * as classNames from "classnames";
import React from 'react';
import ReactDOM from 'react-dom';
import { DndProvider, useDrag, useDrop } from 'react-dnd'
import HTML5Backend from 'react-dnd-html5-backend'

function GridSquare(props) {
    const {
        isPlayerWhite,
        rowNum,
        colNum,
        piece,
    } = props;

    const [{ isDragging }, drag] = useDrag({
        item: { type: 'piece', startRow: rowNum, startCol: colNum },
        collect: monitor => ({
            isDragging: !!monitor.isDragging(),
        }),
    });
    const [{ isOver }, drop] = useDrop({
		accept: 'piece',
		drop: ({startRow, startCol}) => console.log(startRow + " " + startCol + " " + rowNum + " " + colNum),
		collect: monitor => ({
			isOver: !!monitor.isOver(),
		}),
	});

    const canDrag = isPlayerWhite && piece !== piece.toUpperCase() || !isPlayerWhite && piece !== piece.toLowerCase();
    return (
        <span
            ref={drop}
            className={classNames(
                'square',
                (rowNum + colNum) % 2 == 0 ? 'white' : 'black',
                { 'can-drop': isOver },
            )}
        >
            <span
                ref={canDrag ? drag : undefined}
                className={classNames(
                    'piece',
                    piece === ' ' ? undefined : 'piece-' + piece,
                    { 'can-drag': !isDragging && canDrag },
                )}
            >
                {""}
            </span>
        </span>
    );
}

class GridRow extends React.Component {

    render() {
        const { rowNum, row, ...props } = this.props;
        return (
            <div className="grid-row">
                {[...row].map((piece, colNum) => <GridSquare key={colNum} rowNum={rowNum} colNum={colNum} piece={piece} {...props} />)}
            </div>
        );
    }
}

class Grid extends React.Component {

    render() {
        const { grid, ...props } = this.props;
        if (!grid) {
            return null;
        }
        return (
            <DndProvider backend={HTML5Backend}>
                <div className="grid">
                    {grid.map((row, rowNum) => <GridRow key={rowNum} rowNum={rowNum} row={row} {...props} />)}
                </div>
            </DndProvider>
        );
    }
}

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            isPlayerWhite: true,
            grid: ['  n', ' nn', 'n  '],
            isPlayerTurnToMove: true,
            signature: undefined,
        };
    }

    componentDidMount() {
        const { isPlayerWhite } = this.state;
        fetch('/api/penultima/start', {
            method: 'POST',
            body: JSON.stringify({ isPlayerWhite }),
            headers: { 'Content-type': 'application/json' },
        }).then(response => {
            response.json().then(body => {
                this.setState({
                    isPlayerWhite: body.state.isPlayerWhite,
                    grid: body.state.grid,
                    isPlayerTurnToMove: body.state.isPlayerTurnToMove,
                    signature: body.signature,
                });
            });
        });
    }

    render() {
        return (
            <div>
                <div className="header">
                    <h1>Penultima</h1>
                    <span><i>flavor text here</i></span>
                </div>
                <div className="board-container">
                    <Grid {...this.state} />
                </div>
            </div>
        );
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
