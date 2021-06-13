import { get, put, post, deleted } from '@/http/axios'

// 获取组卷规则分页数据
export function handleGenerationRulePage (params) {
  return get('/examPaperBackend/paperGenerationRule/page', params)
}
// 获取组卷规则列表
export function handleGenerationRuleList (params) {
  return get('/examPaperBackend/paperGenerationRule/list', params)
}
// 根据ID获取组卷规则
export function handleGenerationRuleQuery (params) {
  return get('/examPaperBackend/paperGenerationRule/' + params)
}

// 修改组卷规则信息
export function handleUpdateGenerationRule (params) {
  return put('/examPaperBackend/paperGenerationRule', params)
}

// 添加组卷规则信息
export function handleAddGenerationRule (params) {
  return post('/examPaperBackend/paperGenerationRule', params)
}

// 删除组卷规则
export function handleDeleteGenerationRule (params) {
  return deleted('/examPaperBackend/paperGenerationRule/' + params)
}
