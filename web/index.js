import * as classNames from "classnames";
import React from 'react';
import ReactDOM from 'react-dom';
import "./global.css";
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
                className={classNames("main", "fade-in", isHoveringOverPensieve ? "pensieve-hover" : "pensieve")}
            >
                {this.renderName()}
                {this.renderPensieveHoverRegion()}
                {this.renderWisp("wisp1", "cross.html", "King's Cross")}
                {this.renderWisp("wisp2", "sorting.html", "The Sorting Hat")}
                {this.renderWisp("wisp3", "penultima.html", "Wizard's Chess")}
                {this.renderWisp("wisp4", "quidditch.html", "Quidditch")}
                {this.renderWisp("wisp5", "phone.html", "Wizard's Wireless")}
                {this.renderWisp("wisp6", "blind.html", "The Deluminator")}
                {this.renderWisp("wisp7", "time.html", "The Time Turner")}
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
