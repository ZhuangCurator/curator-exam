import Vue from 'vue'
import {
  Button,
  Input,
  Form,
  FormItem,
  Container,
  Header,
  Main,
  Footer,
  Menu,
  MenuItem,
  Card,
  Table,
  TableColumn,
  Pagination,
  Divider,
  Select,
  Option,
  Message,
  MessageBox,
  Tabs,
  TabPane,
  Dialog,
  Row,
  Tag,
  Radio,
  RadioGroup,
  Checkbox,
  CheckboxGroup
} from 'element-ui'

Vue.use(Button)
Vue.use(Input)
Vue.use(Form)
Vue.use(FormItem)
Vue.use(Container)
Vue.use(Header)
Vue.use(Main)
Vue.use(Footer)
Vue.use(Menu)
Vue.use(MenuItem)
Vue.use(Card)
Vue.use(Table)
Vue.use(TableColumn)
Vue.use(Pagination)
Vue.use(Divider)
Vue.use(Select)
Vue.use(Option)
Vue.use(Tabs)
Vue.use(TabPane)
Vue.use(Dialog)
Vue.use(Row)
Vue.use(Tag)
Vue.use(Radio)
Vue.use(RadioGroup)
Vue.use(Checkbox)
Vue.use(CheckboxGroup)

Vue.prototype.$message = Message
Vue.prototype.$confirm = MessageBox.confirm
