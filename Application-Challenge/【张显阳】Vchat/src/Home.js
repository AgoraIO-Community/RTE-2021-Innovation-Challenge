import React from 'react';
import { useManager } from './stores/manager'; 
import { observer } from "mobx-react";
import { useHistory } from "react-router-dom";

const Home = observer(() => {
    const manager = useManager();
    const history = useHistory();

    const [text, setText] = React.useState("");
    return (
        <div>
            <form id="form" onSubmit={async (e) => {
                e.preventDefault();
                setText("加入频道中...");
                await manager.joinRoom();
                setText("加入成功");
                history.push("/room");
            }}>
                <label>房间名</label>
                <input value={manager.roomid} onChange={(e) => {
                    manager.setRoomId(e.target.value);
                }}></input>

                <button form="form" type="submit">加入房间</button>
            </form>
            <p>{text}</p>
        </div>
    );
});

export default Home;
        
            