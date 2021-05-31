//
//  MidiPlay.swift
//  SmartPiano
//
//  Created by Xytec on 16/4/6.
//  Copyright © 2016年 XiYun. All rights reserved.
//

//
//  MIDISampler.swift
//  AVFoundationMIDIPlay
//
//  Created by Gene De Lisa on 1/12/16.
//  Copyright © 2016 Gene De Lisa. All rights reserved.
//

import Foundation
import AVFoundation


class MidiPlay :NSObject{
    
    var engine:AVAudioEngine!
    var sampler:AVAudioUnitSampler!
    
    let melodicBank = UInt8(kAUSampler_DefaultMelodicBankMSB)
    let defaultBankLSB = UInt8(kAUSampler_DefaultBankLSB)
    var loadSuccess: Bool!
    
    /// general midi number for marimba
    @objc let gmMarimba = UInt8()
    let gmHarpsichord = UInt8()
    
    override init() {
        super.init()
        loadSuccess = false
        initAudioEngine()
    }
    
    public func initAudioEngine () {
        
        engine = AVAudioEngine()
        
        sampler = AVAudioUnitSampler()
        engine.attach(sampler)
        
        engine.connect(sampler, to: engine.mainMixerNode, format: nil)
        
        startEngine()
    }
    
    public func startEngine() {
        
        if engine.isRunning {
            print("audio engine already started")
            return
        }
        
        do {
            try engine.start()
            print("audio engine started")
        } catch {
            print("oops \(error)")
            print("could not start audio engine")
        }
    }
    
    
    @objc func loadPatch(_ gmpatch:UInt8, channel:UInt8) {
        
        guard let soundbank =
            Bundle.main.url(forResource: "YDTEC", withExtension: "sf2")
            else {
                print("could not read sound font")
                return
        }
        
        do {
            try sampler.loadSoundBankInstrument(at: soundbank, program:gmpatch,
                                                bankMSB: melodicBank, bankLSB: defaultBankLSB)
            
        } catch let error as NSError {
            print("\(error.localizedDescription)")
            return
        }
        
        self.sampler.sendProgramChange(gmpatch, bankMSB: melodicBank, bankLSB: defaultBankLSB, onChannel: channel)
        loadSuccess = true
    }
    
    
    //    func hstart() {
    //        loadPatch(gmHarpsichord)
    //        self.sampler.startNote(65, withVelocity: 64, onChannel: 0)
    //    }
    //
    //    func hstop() {
    //        self.sampler.stopNote(65, onChannel: 0)
    //    }
    /**
     播放单个midi
     
     - parameter note:     键位
     - parameter velocity: 力度
     - parameter channel:  通道
     */
   @objc public func mstart(_ note:UInt8, velocity:UInt8,channel:UInt8) {
        if loadSuccess == false {
            return
        }
//        loadPatch(gmMarimba, channel:channel)
        self.sampler.startNote(note, withVelocity: velocity, onChannel:channel)
    }
    
    @objc func mstop(_ note:UInt8 ,channel:UInt8) {
        if loadSuccess == false {
            return
        }
        
        self.sampler.stopNote(note, onChannel: channel)
     
    }
    
}


