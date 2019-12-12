import "./style.css";
import * as classNames from "classnames";
import React from 'react';
import ReactDOM from 'react-dom';
import { Wrapper } from "../wrapper";

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
                    <Bulb on={state[3]} />
                </div>
                <div className="bulb-row">
                    <Bulb on={state[1]} />
                    <Bulb on={state[4]} />
                </div>
                <div className="bulb-row">
                    <Bulb on={state[2]} />
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
            complete: false,
            win: false,
        };
    }

    render() {
        const { states, words, complete } = this.state;
        return (
            <Wrapper
                title="Blind Lights Out"
                flavortext="The blind don't need light. Help them turn off all the lights, and they may reward you with a message of their own."
            >
                <div className="coins-panel">
                    {this.renderCoinPanel()}
                </div>
                <div className="bulbs-panel">
                    {states.map(state => <Bulbs state={state} />)}
                </div>
                <div className="input-panel">
                    <input
                        id="input-field"
                        type="text"
                        placeholder="Enter word..."
                        onKeyPress={this.onKeyPress}
                        disabled={complete}
                    />
                </div>
                <div className="history">
                    {words.map(word => <div>{word}</div>)}
                </div>
            </Wrapper>
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
            fetch('/api/blind/answer', {
                method: 'POST',
                body: JSON.stringify({ words: newWords }),
                headers: { 'Content-type': 'application/json' },
            }).then(response => {
                response.json().then(body => {
                    this.setState({
                        states: body.grid || states,
                        words: body.error ? words : newWords,
                        complete: body.complete,
                        win: body.win,
                    });
                    document.getElementById('input-field').value = '';
                    if (body.win) {
                        document.getElementById('input-field').placeholder = '';
                    } else if (body.complete) {
                        document.getElementById('input-field').placeholder = 'Lights out! But not quite fast enough...';
                    } else if (body.error) {
                        document.getElementById('input-field').placeholder = body.error;
                    } else {
                        document.getElementById('input-field').placeholder = 'Enter word...'
                    }
                });
            });
        }
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
