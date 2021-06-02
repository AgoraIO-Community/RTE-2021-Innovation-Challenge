import Service from '@ember/service';
import { inject as service } from '@ember/service';
import { assert } from '@ember/debug';
import {
  FEATURE_LIST,
  LEAN_CLOUD_ERROR_CODE,
} from 'bla-bla-web/utils/constants';
import { underscore } from '@ember/string';
import { isPresent } from '@ember/utils';
export default class GenosService extends Service {
  @service authentication;
  @service liveQuery;

  checkFeatureName(featureName) {
    assert('featureName is a required param', featureName);
    const featureNameKey = underscore(featureName).toUpperCase();
    assert('feature must be on the FEATURE_LIST', FEATURE_LIST[featureNameKey]);
  }

  async createFeatureObj(userId) {
    const Features = this.liveQuery.AV.Object.extend('Features');
    const featuresObj = new Features();
    featuresObj.set('userId', userId);
    return await featuresObj.save();
  }

  /**
   * Try to get features object from LeanCloud by userId.
   * There are two cases when this object doesn't exist
   * case 1: If the feature class does not exist. This happens when that class is query the first time, e.g. when migrate to a new database.
   * case 2: If the featureObject does not exist, this means when can't find feature Object by using the current userId.
   * In both case, we create a new featureObject under this userId
   * @returns features object from LeanCloud
   */
  async getFeatures() {
    const userId = this.authentication.getUserId();
    const featuresQuery = new this.liveQuery.AV.Query('Features');
    featuresQuery.equalTo('userId', userId);
    let features;

    try {
      [features] = await featuresQuery.find();
    } catch (e) {
      if (e.code === LEAN_CLOUD_ERROR_CODE.CLASS_DOES_NOT_EXIST) {
        features = await this.createFeatureObj(userId);
      } else {
        throw e;
      }
    }

    if (!isPresent(features)) {
      features = await this.createFeatureObj(userId);
    }

    return features;
  }

  async showFeature(featureName) {
    this.checkFeatureName(featureName);

    const features = await this.getFeatures();
    const feature = features.get(featureName);

    // if this feature is not exist in the database, it means it is a new feature to this user, so we want to show it
    if (!isPresent(feature)) {
      await this.setFeature(featureName, true);
      return true;
    }

    return feature;
  }

  async setFeature(featureName, isShow) {
    this.checkFeatureName(featureName);

    const features = await this.getFeatures();
    if (features.get(featureName) !== isShow) {
      features.set(featureName, isShow);
      await features.save();
    }
  }
}
