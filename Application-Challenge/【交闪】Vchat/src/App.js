import React, { useState, useEffect, useRef } from 'react';
import './App.css';
import { useManager } from './stores/manager'; 
import { observer } from "mobx-react";

import { faceModel } from "./utils/face";
import { VrmModel } from "./utils/vrm";

const girl = new VrmModel();

let last_upload_time = 0;

const remoteRefMap = {

};

const App = observer(() => {
  const manager = useManager();

  const localPlayer = useRef(null);
  useEffect(async () => {
    if (localPlayer.current) {
      console.log("233 localPlayer", localPlayer);
      await faceModel.init(localPlayer.current);

      requestRef.current = requestAnimationFrame(animate);
      return () => cancelAnimationFrame(requestRef.current);
    }
  }, [localPlayer]);

  const vrmContainer = useRef(null);
  useEffect(async () => {
    if (!vrmContainer.current) {
      return;
    }

    await girl.initVrm("girl1");
    girl.initScene(vrmContainer.current);

  }, [vrmContainer]);

    // Use useRef for mutable variables that we want to persist
  // without triggering a re-render on their change
  const requestRef = React.useRef();
  
  const animate = async () => {
    render();

    let now = Date.now();
    if (now - last_upload_time > 50) {
      manager.upload(girl);
      last_upload_time = now;
    }
    requestRef.current = requestAnimationFrame(animate);
  }

  return (
    <div>
      <div className="local_player hidden">
        <div className="hidden" ref={localPlayer}></div>
      </div>

      <div className="local_player" ref={vrmContainer}></div>
      {Object.keys(manager.vrmList).map((id) => {
        return <div className="remote_player" id={id}>
          <p>id: {id}</p>
        </div>
      })}
    </div>
  );
})

async function render() {
  let result = await faceModel.predict();
  if (result && result[0]) {
    // console.log(result[0]);
    girl.updateMouth(result[0].scaledMesh);
    girl.updateEyes(result[0].scaledMesh);
    girl.updateHead(result[0].scaledMesh);
  }
  girl.render();
}



export default App;
