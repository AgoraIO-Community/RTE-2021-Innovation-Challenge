use std::collections::{HashMap, HashSet};
use std::sync::{Arc, Mutex};

use crate::basic::{create_indexes, get_position, rsp_err, rsp_ok};

use super::basic::VLiveResult;
use super::bean::*;
use chrono::Local;

lazy_static! {
    static ref MODEL: Mutex<Model> = {
        let mut users = HashMap::new();
        let mut channels = HashMap::new();

        let uid = "8086".to_string();
        let name = "UnityUser".to_string();

        let user = Arc::new(User {
            uid: uid.clone(),
            name: name.clone(),
        });
        users.insert(uid, user.clone());

        let scene = "Eden".to_string();
        let mut indexes = create_indexes(&scene).unwrap();
        let member = ChannelMember {
            user: user,
            mode: 1,
            index: indexes.pop().unwrap(),
        };
        let mut members = HashSet::new();
        members.insert(member);

        let channel = Channel {
            id: name.clone(),
            scene: scene,
            desc: "A place where everyone can play freely".to_string(),
            users: members,
            indexes: indexes,
            last_zero_time: Local::now(),
        };
        channels.insert(name, channel);
        Mutex::new(Model {
            users: users,
            channels: channels,
        })
    };
}

const BASE_USER_SIZE: usize = 10000;

pub fn register(data: Vec<u8>) -> VLiveResult {
    let req: UserRegReq = serde_json::from_slice(&data)?;
    let mut model = MODEL.lock().unwrap();
    let uid = (model.users.len() + BASE_USER_SIZE).to_string();
    let user = User {
        uid: uid.clone(),
        name: req.name,
    };
    println!("Add user: {:?}", &user);

    model.users.insert(uid.clone(), Arc::new(user));
    rsp_ok(UserRegRsp { uid })
}

pub fn create_channel(data: Vec<u8>) -> VLiveResult {
    let req: ChannelCreateReq = serde_json::from_slice(&data)?;
    let mut model = MODEL.lock().unwrap();

    let index_set = match create_indexes(&req.scene) {
        Some(v) => v,
        None => return rsp_err("Scene not found"),
    };
    if let Some(_) = model.channels.get(&req.cid) {
        return rsp_err("Channel has existed");
    }

    let cid = req.cid.clone();
    let channel = Channel {
        id: req.cid,
        scene: req.scene,
        desc: req.desc,
        users: HashSet::new(),
        indexes: index_set,
        last_zero_time: Local::now(),
    };

    println!("Create channel {:?}", &channel);
    model.channels.insert(cid, channel);
    rsp_ok(String::new())
}

pub fn join_channel(data: Vec<u8>) -> VLiveResult {
    let req: ChannelJoinReq = serde_json::from_slice(&data)?;
    let mut model = MODEL.lock().unwrap();

    let user = match model.users.get(&req.uid) {
        Some(v) => v,
        None => return rsp_err("User not exist"),
    }
    .clone();

    model.channels.get_mut(&req.cid).map_or_else(
        || rsp_err("Channel not exist"),
        |c| {
            if c.has_user(&user.uid) {
                return rsp_err("Duplicate join");
            }
            let (index, pos) = match get_position(c) {
                Some(p) => p,
                None => return rsp_err("No space available"),
            };
            let rsp = ChannelJoinRsp {
                pos,
                users: c
                    .users
                    .iter()
                    .map(|c| ChannelUserInfo {
                        uid: c.user.uid.clone(),
                        name: c.user.name.clone(),
                        mode: c.mode,
                    })
                    .collect(),
            };
            c.users.insert(ChannelMember::new(user, req, index));
            rsp_ok(rsp)
        },
    )
}

pub fn leave_channel(data: Vec<u8>) -> VLiveResult {
    let req: ChannelLeaveReq = serde_json::from_slice(&data)?;
    let mut model = MODEL.lock().unwrap();

    model.channels.get_mut(&req.cid).map_or_else(
        || rsp_err("Channel not exist"),
        |c| {
            c.remove_user(&req.uid);
            rsp_ok(String::new())
        },
    )
}

pub fn list_channel(_: Vec<u8>) -> VLiveResult {
    let model = MODEL.lock().unwrap();
    let mut rsp = Vec::new();

    model.channels.iter().for_each(|c| {
        let c = c.1;
        rsp.push(ChannelListRsp {
            cid: c.id.clone(),
            desc: c.desc.clone(),
            count: c.users.len(),
            max_count: c.users.len() + c.indexes.len(),
        });
    });

    rsp_ok(rsp)
}
