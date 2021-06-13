import { get, put, post, deleted } from '@/http/axios'

// 获取规则详情分页数据
export function handleGenerationRuleDetailPage (params) {
  return get('/examPaperBackend/paperGenerationRuleDetail/page', params)
}
// 获取规则详情列表
export function handleGenerationRuleDetailList (params) {
  return get('/examPaperBackend/paperGenerationRuleDetail/list/' + params)
}
// 根据ID获取规则详情
export function handleGenerationRuleDetailQuery (params) {
  return get('/examPaperBackend/paperGenerationRuleDetail/' + params)
}

// 修改规则详情信息
export function handleUpdateGenerationRuleDetail (params) {
  return put('/examPaperBackend/paperGenerationRuleDetail', params)
}

// 添加规则详情信息
export function handleAddGenerationRuleDetail (params) {
  return post('/examPaperBackend/paperGenerationRuleDetail', params)
}

// 删除规则详情
export function handleDeleteGenerationRuleDetail (params) {
  return deleted('/examPaperBackend/paperGenerationRuleDetail/' + params)
}
