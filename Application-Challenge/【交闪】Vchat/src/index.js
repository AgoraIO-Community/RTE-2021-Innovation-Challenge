import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import { ManagerStore } from "./stores/manager";
import { Provider } from "mobx-react";
import App from './App';
import Home from "./Home";
import {
  HashRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";

const defaultStore = {
  manager: new ManagerStore(),
}

function Main() {
  return (
    <Provider store={defaultStore}>
      <Router>
        <Switch>
          <Route path="/room">
            <App />
          </Route>
          <Route path="/">
            <Home />
          </Route>
        </Switch>
      </Router>

    </Provider>
  );
}

ReactDOM.render(
  <React.StrictMode>
    <Main />
  </React.StrictMode>,
  document.getElementById('root')
);