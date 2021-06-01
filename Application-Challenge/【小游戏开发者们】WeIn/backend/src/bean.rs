use std::collections::HashSet;

use serde::{Deserialize, Serialize};

use std::collections::HashMap;

use chrono::{DateTime, Local};
use std::sync::Arc;

pub struct Model {
    pub users: HashMap<String, Arc<User>>,
    pub channels: HashMap<String, Channel>,
}

#[derive(Debug, PartialEq, Eq, Hash)]
pub struct User {
    pub uid: String,
    pub name: String,
}

#[derive(Debug)]
pub struct Channel {
    pub id: String,
    pub scene: String,
    pub desc: String,
    pub users: HashSet<ChannelMember>,
    pub indexes: Vec<usize>,
    pub last_zero_time: DateTime<Local>,
}

#[derive(Debug, PartialEq, Eq, Hash)]
pub struct ChannelMember {
    pub user: Arc<User>,
    pub mode: usize,
    pub index: usize,
}

#[derive(Deserialize)]
pub struct UserRegReq {
    pub name: String,
}

#[derive(Serialize)]
pub struct UserRegRsp {
    pub uid: String,
}

#[derive(Deserialize)]
pub struct ChannelCreateReq {
    pub cid: String,
    pub scene: String,
    pub desc: String,
}

#[derive(Deserialize)]
pub struct ChannelJoinReq {
    pub uid: String,
    pub cid: String,
    pub mode: usize,
}

#[derive(Serialize)]
pub struct ChannelJoinRsp {
    pub pos: Vec<f32>,
    pub users: Vec<ChannelUserInfo>,
}

#[derive(Serialize)]
pub struct ChannelUserInfo {
    pub uid: String,
    pub name: String,
    pub mode: usize,
}

#[derive(Deserialize)]
pub struct ChannelLeaveReq {
    pub uid: String,
    pub cid: String,
}

#[derive(Serialize)]
pub struct ChannelListRsp {
    pub cid: String,
    pub desc: String,
    pub count: usize,
    pub max_count: usize,
}

impl ChannelMember {
    pub fn new(user: Arc<User>, req: ChannelJoinReq, index: usize) -> Self {
        ChannelMember {
            user,
            mode: req.mode,
            index,
        }
    }
}

impl Channel {
    pub fn has_user(&self, uid: &String) -> bool {
        self.users.iter().any(|u| &u.user.uid == uid)
    }

    pub fn remove_user(&mut self, uid: &String) {
        let index = match self.users.iter().find(|u| &u.user.uid == uid) {
            Some(c) => c.index,
            None => return,
        };
        self.users.retain(|c| &c.user.uid != uid);
        self.indexes.push(index)
    }
}
