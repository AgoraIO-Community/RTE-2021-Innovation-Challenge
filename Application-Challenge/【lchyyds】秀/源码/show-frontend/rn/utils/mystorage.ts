import Storage from 'react-native-storage';
import AsyncStorage from '@react-native-community/async-storage';

class MyStorage {
  private static instance: Storage;

  private static watchersMap: Map<string, Function[]>;

  private static getInstance = () => {
    if (!MyStorage.instance) {
      MyStorage.instance = new Storage({
        // maximum capacity, default 1000 key-ids
        size: 1000,

        // Use AsyncStorage for RN apps, or window.localStorage for web apps.
        // If storageBackend is not set, data will be lost after reload.
        storageBackend: AsyncStorage, // for web: window.localStorage

        // expire time, default: 1 day (1000 * 3600 * 24 milliseconds).
        // can be null, which means never expire.
        // defaultExpires: 1000 * 3600 * 24,

        // cache data in the memory. default is true.
        enableCache: true,

        // if data was not found in storage or expired data was found,
        // the corresponding sync method will be invoked returning
        // the latest data.
        sync: {
          // we'll talk about the details later.
        }
      });
    }
    return MyStorage.instance;
  };

  private static getWatchersMap = () => {
    if (!MyStorage.watchersMap) {
      MyStorage.watchersMap = new Map();
    }
    return MyStorage.watchersMap;
  };

  static setItem: (key: string, value: any) => void
    = (key, value) => {
      MyStorage.getInstance()
        .save({
          key, // Note: Do not use underscore("_") in key!
          data: value,
        })
        .then(() => {
          const watchers = MyStorage.getWatchersMap().get(key);
          watchers?.forEach((cb) => cb(value));
        });

    };

  static getItem: (key: string) => Promise<string> =
    (key) => {
      return MyStorage.getInstance()
        .load({ key })
        .then(ret => {
          // found data goes to then()
          return ret;
        })
        .catch(err => {
          // any exception including data not found
          // goes to catch()
          console.warn(err.message);
          switch (err.name) {
            case 'NotFoundError':
              // TODO
              break;
            case 'ExpiredError':
              // TODO
              break;
          }
          return Promise.reject(err);
        });
    };

  static watchItem: (key: string, callback: (newData: any) => void) => any
    = (key, callback) => {
      const watchers = MyStorage.getWatchersMap().get(key);
      if (watchers) {
        MyStorage.getWatchersMap().set(key, [...watchers, callback]);
      }
      else {
        MyStorage.getWatchersMap().set(key, [callback]);
      }
      return MyStorage.getItem(key);
    };
};

export default MyStorage;
