
export function saveToken (token) {
  sessionStorage.setItem('token', token)
}

export function getToken () {
  const token = sessionStorage.getItem('token')
  if (!token) return ''
  return token
}
