<!--如果选择上链，则展示该操作栏，完成上链操作后才可以提交到后台（包括address和tx）-->
<template>
  <div class="display-flex">
    <div class="title_name">上链操作</div>
    <el-button @click="linkMetaMask()" type="primary"
               v-if="!isFinishLinkMetaMask"
               :loading="isLoadingMetaMask">连接到MetaMask
    </el-button>
    <el-button icon="el-icon-check" v-if="isFinishLinkMetaMask" type="primary" disabled>关联地址:{{ this.linkedAddress }}
    </el-button>
    <el-button type="primary" v-if="isFinishLinkMetaMask && !isFinishBuildContract"
               @click="createCourse()">创建合约
    </el-button>
    <el-button type="primary" v-if="isFinishBuildContract"
               disabled="disabled">关联合约:{{ this.txAddress }}
    </el-button>
  </div>
</template>

<script>
import sol from '@/contracts/CourseCreate.json'

export default {
  name: "metamask_options",
  props: [
    'title_name',
    'textarea',
    'class_money',
    'class_title_name',
    'class_title_name2',
    'editor',
    'avatar',
    'value1',
    'afterBuyClass',
    'class_title_type'
  ],
  data() {
    return {
      // todo: token address 改为后端配置
      tokenAddress: '0xDfdD266b2fa2852C4fAA6706F851127E1DCB7e5a',
      adminAddress: '0xe46EF0abF04f602268C1198887dC1Bb2f51b778d',
      linkedAddress: null,
      isLoadingMetaMask: false,
      isFinishLinkMetaMask: false,
      isFinishBuildContract: false,
      txAddress: null
    }
  },
  mounted() {

  },
  watch: {
    title_name: function (newVal, oldVal) {
      this.title_name = newVal;
    },
    textarea: function (newVal, oldVal) {
      this.textarea = newVal;
    },
    class_money: function (newVal, oldVal) {
      this.class_money = newVal;
    },
    class_title_name: function (newVal, oldVal) {
      this.class_title_name = newVal;
    },
    class_title_name2: function (newVal, oldVal) {
      this.class_title_name2 = newVal;
    },
    editor: function (newVal, oldVal) {
      this.editor = newVal;
    },
    avatar: function (newVal, oldVal) {
      this.avatar = newVal;
    },
    afterBuyClass: function (newVal, oldVal) {
      this.afterBuyClass = newVal;
    },
    class_title_type: function (newVal, oldVal) {
      this.class_title_type = newVal;
    },
    value1: function (newVal, oldVal) {
      this.value1 = newVal;
    }
  },
  methods: {
    async createCourse() {
      if (!this.title_name ||
          !this.textarea ||
          !this.class_money ||
          this.class_title_name2 === "请选择" ||
          !this.editor ||
          !this.avatar ||
          this.value1.length < 2 ||
          !this.afterBuyClass ||
          this.class_title_type === "请选择") {
        this.$message({
          showClose: true,
          message: '请先完善信息',
          type: 'error'
        });
        return;
      }

      console.log("创建课程");
      // console.log(sol);
      console.log('bytecode : ', sol['bytecode'])
      console.log('abi : ', sol['abi'])
      console.log(this.value1)
      let Web3 = this.Web3;
      let web3 = new Web3(Web3.givenProvider);
      if (!web3) {
        web3 = new Web3(window.ethereum);
      }
      let bytecode = sol['bytecode'];
      let cInterface = sol['abi'];
      let contract = new web3.eth.Contract(cInterface)
      const that = this;
      // todo: 部署合约
      // todo: 参数矫正
      const loading = this.$loading({
        lock: true,
        text: '正在创建合约，请不要关闭窗口……',
        spinner: 'el-icon-loading',
        background: 'rgba(101,12,12,0.7)'
      });
      // const args = [
      //   that.tokenAddress,
      //   '', '', '', '', '',
      //   // 'titleName', 'textArea', 'classTitleName', 'classTitleName2', '',
      //   that.value1[0].getTime() / 1000, that.value1[1].getTime() / 1000,
      //   that.afterBuyClass,
      //   that.adminAddress
      // ];
      const args = [
        that.tokenAddress,
        '', '', '', '', '',
        that.value1[0].getTime() / 1000, that.value1[1].getTime() / 1000,
        that.afterBuyClass,
        that.adminAddress,
        that.class_money
      ];
      // const args = [
      //   that.tokenAddress,
      //   '1', '2', '3', '4', '5',
      //   0, 0, 0,
      //   that.adminAddress
      // ];
      console.log(args);
      console.log("gasLimit:"+(await web3.eth.getBlock("latest")).gasLimit);
      try {
        await contract.deploy({
          data: bytecode,
          arguments: args,
          // arguments: [
          //   that.tokenAddress.toString(),
          //   that.title_name.toString(), that.textarea.toString(), that.class_title_name.toString(),
          //   that.class_title_name2.toString(), '',
          //   that.value1[0].getTime() / 1000, that.value1[1].getTime() / 1000,
          //   that.afterBuyClass,
          //   that.adminAddress.toString()
          // ]
        }).send({
          from: this.linkedAddress,
          gas: 3000000,
        }).then(instance => {
          console.log("contract address : ", instance.options.address);
          if (instance.options.address) {
            this.$message({
              showClose: true,
              message: '创建合约地址成功!',
              type: 'success'
            });
            this.emitCourseTxAddress(instance.options.address);
            this.isFinishBuildContract = true;
            this.txAddress = instance.options.address;
          }

          loading.close();
        }).finally(() => loading.close())
      } catch (exception) {
        this.$message({
          showClose: true,
          message: '出现异常',
          type: 'error'
        });
        console.log(exception);
        loading.close();
      }


    },
    async linkMetaMask() {
      let Web3 = this.Web3;
      this.isLoadingMetaMask = true;
      /** @var Web3  **/
      if (Web3) {
        let web3 = new Web3(Web3.givenProvider);
        try {
          // Request account access if needed
          await window.ethereum.enable()
        } catch (error) {
          this.$message({
            showClose: true,
            message: '请允许通过DApp链接你的MetaMask账号',
            type: 'error'
          });
          this.isLoadingMetaMask = false;
          return
        }
        if (!web3) {
          web3 = new Web3(window.ethereum);
        }

        web3.eth.getAccounts().then((accounts) => {
          console.log(accounts)
          // 列出所有账户地址 array
          // 如果存在多账户，则返回多个
          if (accounts && accounts[0]) {
            this.linkedAddress = accounts[0];
            this.emitCourseCreatorAddress(this.linkedAddress);
            this.isFinishLinkMetaMask = true;
          }
        }).finally(() => {
          this.isLoadingMetaMask = false
        });
      } else {
        this.isLoadingMetaMask = false;
      }
    },
    emitCourseTxAddress(txAddress) {
      this.$emit('course-create-event', txAddress);
    },
    emitCourseCreatorAddress(creatorAddress) {
      this.$emit('course-link-creator-event', creatorAddress);
    }
  }
}
</script>

<style scoped>
.title_name {
  margin-right: 50px;
  font-size: 18px;
  color: #282828;
}
</style>