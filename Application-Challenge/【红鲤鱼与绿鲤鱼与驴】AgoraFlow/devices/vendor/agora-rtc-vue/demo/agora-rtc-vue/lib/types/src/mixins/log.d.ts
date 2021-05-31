import { Vue } from "vue-property-decorator";
export default class Log extends Vue {
    __log(...argu: any[]): void;
    __warn(...argu: any[]): void;
    __error(...argu: any[]): void;
    __info(...argu: any[]): void;
}
