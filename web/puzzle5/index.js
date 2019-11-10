import "./style.css";
import * as classNames from "classnames";

class GridSquare extends React.Component {

    render() {
        const { rowNum, colNum, piece } = this.props;
        return (
            <span className={classNames(
                'square',
                (rowNum + colNum) % 2 == 0 ? 'white' : 'black',
                piece === ' ' ? undefined : 'piece-' + piece,
            )}>
                {""}
            </span>
        );
    }
}

class GridRow extends React.Component {

    render() {
        const { rowNum, row } = this.props;
        return (
            <div className="grid-row">
                {[...row].map((piece, colNum) => <GridSquare rowNum={rowNum} colNum={colNum} piece={piece} />)}
            </div>
        );
    }
}

class Grid extends React.Component {

    render() {
        const { grid } = this.props;
        if (!grid) {
            return null;
        }
        return (
            <div className="grid">
                {grid.map((row, rowNum) => <GridRow rowNum={rowNum} row={row} />)}
            </div>
        );
    }
}

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            isPlayerWhite: true,
            grid: undefined,
            isPlayerTurnToMove: true,
            signature: undefined,
        };
    }

    componentDidMount() {
        const { isPlayerWhite } = this.state;
        fetch('/api/puzzle5/start', {
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
        const { grid } = this.state;
        return (
            <div>
                <div className="header">
                    <h1>Penultima</h1>
                    <span><i>flavor text here</i></span>
                </div>
                <div className="board-container">
                    <Grid grid={grid} />
                </div>
            </div>
        );
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
