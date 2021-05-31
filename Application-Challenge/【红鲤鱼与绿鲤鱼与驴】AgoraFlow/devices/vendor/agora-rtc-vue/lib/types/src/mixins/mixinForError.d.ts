import { Vue } from "vue-property-decorator";
export default class MixinForError extends Vue {
    errorHandler: (error: Error, vm: Vue, info: string) => boolean | void;
}
