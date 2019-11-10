import "./style.css";
import * as classNames from "classnames";
import React from 'react';
import ReactDOM from 'react-dom';
import { DndProvider, useDrag, useDrop } from 'react-dnd'
import HTML5Backend from 'react-dnd-html5-backend'

function GridSquare(props) {
    const {
        state: {
            playerWhite,
            playerTurnToMove,
        },
        rowNum,
        colNum,
        piece,
        playerMove,
    } = props;

    const [{ isDragging }, drag] = useDrag({
        item: { type: 'piece', startRow: rowNum, startCol: colNum },
        collect: monitor => ({
            isDragging: !!monitor.isDragging(),
        }),
    });
    const [{ isOver }, drop] = useDrop({
		accept: 'piece',
		drop: ({startRow, startCol}) => playerMove(startRow, startCol, rowNum, colNum),
		collect: monitor => ({
			isOver: !!monitor.isOver(),
		}),
	});

    const canDrag = playerTurnToMove &&
        (playerWhite && piece !== piece.toUpperCase() || !playerWhite && piece !== piece.toLowerCase());
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
        const { state, ...props } = this.props;
        const { grid } = state;
        if (!grid) {
            return null;
        }
        return (
            <DndProvider backend={HTML5Backend}>
                <div className="grid">
                    {grid.map((row, rowNum) => <GridRow key={rowNum} rowNum={rowNum} row={row} state={state} {...props} />)}
                </div>
            </DndProvider>
        );
    }
}

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            state: {
                playerWhite: true,
                grid: undefined,
                playerTurnToMove: true,
            },
            signature: undefined,
            isMoveValid: undefined,
        };
    }

    componentDidMount() {
        const { state: { playerWhite } } = this.state;
        // TODO use relative URIs
        fetch('http://localhost:8090/api/penultima/start', {
            method: 'POST',
            body: JSON.stringify({ playerWhite }),
            headers: { 'Content-type': 'application/json' },
        }).then(response => {
            response.json().then(body => {
                this.setState({
                    state: body.state,
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
                    <Grid {...this.state} playerMove={this.playerMove} />
                </div>
            </div>
        );
    }

    playerMove = (startRow, startCol, endRow, endCol) => {
        const { state, signature } = this.state;
        fetch('http://localhost:8090/api/penultima/player-move', {
            method: 'POST',
            body: JSON.stringify({
                startState: state,
                signature,
                move: {
                    start: { row: startRow, col: startCol },
                    end: { row: endRow, col: endCol },
                },
            }),
            headers: { 'Content-type': 'application/json' },
        }).then(response => {
            response.json().then(body => {
                this.setState({
                    isMoveValid: body.isValid,
                    state: body.endState,
                    signature: body.signature,
                });
            });
        });
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
