import "./style.css";
import * as classNames from "classnames";

class Bulb extends React.Component {

    render() {
        return <span className={classNames("bulb", this.props.on ? "on" : "off")}></span>;
    }
}

class Bulbs extends React.Component {

    render() {
        const { state } = this.props;
        return (
            <div className="bulb-section">
                <div className="bulb-row">
                    <Bulb on={state[0]} />
                    <Bulb on={state[1]} />
                </div>
                <div className="bulb-row">
                    <Bulb on={state[2]} />
                    <Bulb on={state[3]} />
                </div>
                <div className="bulb-row">
                    <Bulb on={state[4]} />
                    <Bulb on={state[5]} />
                </div>
            </div>
        );
    }
}

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            states: [
                [true, true, true, true, true, true],
                [true, true, true, true, true, true],
                [true, true, true, true, true, true],
                [true, true, true, true, true, true],
                [true, true, true, true, true, true],
                [true, true, true, true, true, true],
                [true, true, true, true, true, true],
                [true, true, true, true, true, true],
            ],
            words: [],
            win: false,
        };
    }

    render() {
        const { states, words, win } = this.state;
        return (
            <div>
                <div className="block header">
                    <h1>Blind Lights Out</h1>
                </div>
                <div className="block coins-panel">
                    {this.renderCoinPanel()}
                </div>
                <div className="block bulbs-panel">
                    {states.map(state => <Bulbs state={state} />)}
                </div>
                <div className="block input-panel">
                    <input
                        id="input-field"
                        type="text"
                        placeholder="Enter word..."
                        onKeyPress={this.onKeyPress}
                        disabled={win}
                    />
                </div>
                <div className="block history">
                    {words.map(word => <div>{word}</div>)}
                </div>
            </div>
        );
    }

    renderCoinPanel() {
        const { words, win } = this.state;
        if (win) {
            return <span className="happy"></span>;
        } else if (words.length < 8) {
            return [...new Array(8 - words.length)].map(i => <span className="coin"></span>);
        } else {
            return <span className="sad"></span>;
        }
    }

    onKeyPress = e => {
        const { states, words } = this.state;
        if (e.key === 'Enter') {
            const newWord = e.target.value.trim().toUpperCase();
            const newWords = [newWord, ...words];
            fetch('/api/answer', {
                method: 'POST',
                body: JSON.stringify({ words: newWords }),
                headers: { 'Content-type': 'application/json' },
            }).then(response => {
                response.json().then(body => {
                    this.setState({
                        states: body.grid || states,
                        words: body.error ? words : newWords,
                        win: body.win,
                    });
                    document.getElementById('input-field').value = '';
                    document.getElementById('input-field').placeholder = body.win ? '' : (body.error || 'Enter word...');
                });
            });
        }
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
