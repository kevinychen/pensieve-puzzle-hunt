import * as classNames from "classnames";
import React from 'react';
import ReactDOM from 'react-dom';
import "./style.css";

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: '',
            isHoveringOverPensieve: false,
        };
    }

    render() {
        const { isHoveringOverPensieve } = this.state;
        return (
            <div
                className={classNames("main", isHoveringOverPensieve ? "pensieve-hover" : "pensieve")}
            >
                {this.renderName()}
                {this.renderPensieveHoverRegion()}
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

    renderPensieveHoverRegion() {
        return (
            <svg
                className="clickable-pensieve"
                width="1012"
                height="680"
            >
                <a href="pensieve.html">
                    <polygon
                        onMouseEnter={() => this.setState({ name: 'The Pensieve', isHoveringOverPensieve: true })}
                        onMouseLeave={() => this.setState({ name: '', isHoveringOverPensieve: false })}
                        points="40,440 140,550 250,620 360,650 640,650 770,620 890,550 990,440 520,500"
                        fillOpacity="0"
                    />
                </a>
            </svg>
        );
    }

    renderWisp(id, href, name) {
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
