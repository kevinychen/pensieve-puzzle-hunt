import React from 'react';
import ReactDOM from 'react-dom';
import { createGlobalStyle } from 'styled-components';
import { Wrapper } from "../wrapper";
import "./style.css";

import GlennSansBold from "./BwGlennSans-Bold.otf";
import GlennSansMedium from "./BwGlennSans-Medium.otf";
const GlobalStyle = createGlobalStyle`
    @font-face {
        font-family: 'GlennSansBold';
        src: url('${GlennSansBold}');
    }

    @font-face {
        font-family: 'GlennSans';
        src: url('${GlennSansMedium}');
    }
`;

class Puzzle extends React.Component {

    render() {
        const { content, numBoxes, isLast } = this.props;
        return (
            <div>
                <div className="puzzle">
                    <div>{content}</div>
                    {[...new Array(numBoxes || 1)].map(i => <div className="box"></div>)}
                    {isLast ? undefined : <hr className="line" />}
                </div>
            </div>
        );
    }

    renderBox() {
        return <div className="box"></div>;
    }
}

import Image1 from "./image1.png";
import Image2 from "./image2.png";
import Song5 from "./song5.mp3";
import Image5 from "./image5.png";
import Image7 from "./image7.png";
import Image9 from "./image9.png";

class App extends React.Component {

    render() {
        return (
            <Wrapper
                title="The Time Turner"
                flavortext={<h3>solved by <b>Team Runpeng</b></h3>}
            >
                <GlobalStyle />
                <hr></hr>
                <p className="flavor-text">Time to do this again!</p>

                <Puzzle
                    content={<img src={Image1} />}
                />
                <Puzzle
                    content={<img className="narrow white" src={Image2} />}
                />
                <Puzzle
                    content={<pre className="narrow black">2kr4/pbp5/1p6/3p2p1/3N2P1/1PP1QB2/5PP1/6RK b - -</pre>}
                />
                <Puzzle
                    content={
                        <div className="puzzle4 white">
                            <pre className="align-left">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;e = 2915<br /><br />
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;N = 145185003465065910011450192349009309151<br /><br />
                                ciphertext = 115222396423564993230828050014743162641
                            </pre>
                        </div>
                    }
                    numBoxes={3}
                />
                <Puzzle
                    content={
                        <div>
                            <div className="block"><a href={Song5}>An encore</a></div>
                            <div><img src={Image5} /></div>
                        </div>
                    }
                />
                <Puzzle
                    content={<img className="narrow" src={"./files/Wonderland.png"} />}
                />
                <Puzzle
                    content={<img className="bordered" src={Image7} />}
                />
                <Puzzle
                    content={<img src={"./files/d7778742049d.png"} />}
                />
                <Puzzle
                    content={<img className="narrow" src={Image9} />}
                    isLast={true}
                />
            </Wrapper>
        );
    }
}

ReactDOM.render(<App></App>, document.getElementById('app'));
