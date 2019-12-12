import "./global.css";
import React from 'react';
import { createGlobalStyle } from 'styled-components';

import Potter from "./potter.ttf";
const GlobalStyle = createGlobalStyle`
    @font-face {
        font-family: 'Potter';
        src: url('${Potter}');
    }
`;

export class Wrapper extends React.Component {

    render() {
        const { children, title, flavortext } = this.props;
        return (
            <div>
                <GlobalStyle />
                <div className="header">
                    <h1>{title}</h1>
                    <span><i>{flavortext}</i></span>
                </div>
                {children}
            </div>
        );
    }
}
