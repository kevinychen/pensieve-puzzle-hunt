import React from 'react';
import ReactDOM from 'react-dom';
import { Wrapper } from "../wrapper";
import "./style.css";

import Image1 from "./image1.png";
import Image2 from "./image2.png";

class Row extends React.Component {

    render() {
        const { src, text } = this.props;
        return (
            <div className="group">
                <span><img src={src} /><span className="big">{text}</span></span>
                <span className="big">→</span>
                <div className="box"></div>
            </div>
        );
    }
}

class App extends React.Component {

    render() {
        return (
            <Wrapper
                title="The Pensieve"
                flavortext="After extracting all the memories from the Pensieve, you finally have what you need to
                    spell out your final goal. There's no time to wander; you need to move quickly."
            >
                <Row src={Image1} text="(7)" />
                <Row src={Image2} text="(7)" />
                <Row src={Image1} text="(7)" />
                <Row src={Image1} text="(8)" />
                <Row src={Image1} text="(7)" />
                <Row src={Image1} text="(7 8)" />
                <Row src={Image1} text="(8)" />
            </Wrapper>
        );
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
