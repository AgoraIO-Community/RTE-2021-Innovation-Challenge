use std::collections::HashMap;

use serde::Serialize;
use serde_json::Error;

use crate::bean::Channel;

pub type VLiveResult = Result<Vec<u8>, VLiveErr>;

#[derive(Serialize)]
pub struct VLiveRsp<T: Serialize> {
    code: i32,
    msg: String,
    data: T,
}

pub fn rsp_ok<T: Serialize>(msg: T) -> VLiveResult {
    let rsp = VLiveRsp {
        code: 0,
        msg: "".to_string(),
        data: msg,
    };
    Ok(serde_json::to_vec(&rsp).unwrap())
}

pub fn rsp_err(msg: &str) -> VLiveResult {
    Err(VLiveErr {
        code: -1,
        msg: String::from(msg),
    })
}

#[derive(Serialize)]
pub struct VLiveErr {
    pub code: i32,
    pub msg: String,
}

impl VLiveErr {
    pub fn not_found(s: &str) -> Self {
        VLiveErr {
            code: -2,
            msg: format!("Function {} not found", s),
        }
    }
    pub fn err(s: &str) -> Self {
        VLiveErr {
            code: -1,
            msg: s.to_string(),
        }
    }
}

impl std::convert::From<serde_json::Error> for VLiveErr {
    fn from(_: Error) -> Self {
        VLiveErr {
            code: -1,
            msg: "Serialize/Deserialize error!".to_string(),
        }
    }
}

// helper function to read nohup.log
pub fn read_log(_: Vec<u8>) -> VLiveResult {
    std::fs::read("/log/vlive.log").map_err(|_| VLiveErr::err("File not found"))
}

// helper function to clear nohup.log
pub fn remove_log(_: Vec<u8>) -> VLiveResult {
    std::fs::write("/log/vlive.log", "").map_or_else(
        |_| rsp_err("Write to log file failed"),
        |_| rsp_ok("Remove log success"),
    )
}

lazy_static! {
    static ref ROOMS: HashMap<String, Vec<Vec<f32>>> = [(
        "Eden".to_string(),
        vec![
            vec![0.0, 0.0, -4.0],
            vec![0.0, 0.0, 4.0],
            vec![4.0, 0.0, 0.0],
            vec![-4.0, 0.0, 0.0]
        ]
    ),]
    .iter()
    .cloned()
    .collect();
}

pub fn create_indexes(s: &String) -> Option<Vec<usize>> {
    ROOMS.get(s).map(|v| (0..v.len()).collect())
}

pub fn get_position(c: &mut Channel) -> Option<(usize, Vec<f32>)> {
    let idx = match c.indexes.pop() {
        Some(v) => v,
        None => return None,
    };
    match ROOMS.get(&c.scene) {
        Some(vec) => match vec.get(idx) {
            Some(v) => Some((idx, v.clone())),
            None => None,
        },
        None => None,
    }
}
