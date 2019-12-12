import "./style.css";
import React from 'react';
import ReactDOM from 'react-dom';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: '',
        };
    }

    render() {
        const { name } = this.state;
        return (
            <div className="main">
                {this.renderName()}
                {this.renderWisp("wisp1", "blind.html", "Blind Lights Out")}
                {this.renderWisp("wisp2", "penultima.html", "Penultima")}
                {this.renderWisp("wisp3", "phone.html", "Noisy Phone Line")}
                {this.renderWisp("wisp4", "quidditch.html", "Quidditch")}
                {this.renderWisp("wisp5", "sorting.html", "The Sorting Hat")}
                {this.renderWisp("wisp6", "time.html", "The Time Turner")}
                {this.renderWisp("wisp7", "#", "unknown")}
            </div>
        );
    }

    renderName() {
        const { name } = this.state;
        if (name) {
            return <div className="name">{name}</div>
        }
    }

    renderWisp = (id, href, name) => {
        return (
            <a
                id={id}
                className="wisp"
                href={href}
                onMouseEnter={() => this.setState({ name })}
                onMouseLeave={() => this.setState({ name: '' })}
            />
        );
    };
}

ReactDOM.render(<App></App>, document.getElementById('app'));
