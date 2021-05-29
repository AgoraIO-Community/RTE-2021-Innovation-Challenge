import Vue from "vue";
export default class MixinTopErrorComponent extends Vue {
    errorCaptured(err: Error, vm: Vue, info: string): boolean;
}
