import axios from "axios";

const url = location.protocol + '//' + location.hostname + ':8081'
const root = url + '/braze/api/'
const constants = root + 'constants'
const mods = root + 'mods'
const packs = root + 'packs'
const latency = 1600 // artifical latency

if (url !== location.origin) { // for dev environment
  axios.defaults.headers.common['Authorization'] = 'Basic dXNlcjpjaGFuZ2VtZQ=='
}

function get (url: string, success: any, error: any) {
  loadAndThen(() =>
    axios.get(url).then(success).catch(error)
  )
}

function post (url: string, data: any, success: any, error: any) {
  loadAndThen(() =>
    axios.post(url, data).then(success).catch(e => {
      console.error(e.response.data)
      error(e)
    })
  )
}

function loadAndThen (cb: any) {
  setTimeout(cb, latency)
}

export function getConstants (success: any, error: any) {
  get(constants, success, error)
}

export function getMods (success: any, error: any) {
  get(mods, success, error)
}

export function getMod (slug: any, success: any, error: any) {
  get(`${mods}/${slug}`, success, error)
}

export function getPack (slug: any, success: any, error: any) {
  get(`${packs}/${slug}`, success, error)
}

export function getPacks (success: any, error: any) {
  get(packs, success, error)
}

export function postMod (data: any, success: any, error: any) {
  post(mods, data, success, error)
}

export function postModVersion (slug: any, data: any, success: any, error: any) {
  post(`${mods}/${slug}`, data, success, error)
}

export function postPack (data: any, success: any, error: any) {
  post(packs, data, success, error)
}

export function postPackVersion (slug: any, data: any, success: any, error: any) {
  post(`${packs}/${slug}`, data, success, error)
}

function slugify (str: string) {
  return str.toLowerCase()
    .replace(/[^\w\s-]/g, '') // remove non-word [a-z0-9_], non-whitespace, non-hyphen characters
    .replace(/[\s_-]+/g, '-') // swap any length of whitespace, underscore, hyphen characters with a single -
    .replace(/^-+|-+$/g, '') //  remove leading, trailing -
  // function written by kganser https://gist.github.com/mathewbyrne/1280286#gistcomment-1716050
}
