import axios from 'axios';

const baseUrl = `${window.location.protocol}//${window.location.hostname}:8081`;
const root = `${baseUrl}/braze/api/`;
const constants = `${root}constants`;
const mods = `${root}mods`;
const packs = `${root}packs`;
const latency = 1600; // artifical latency

if (baseUrl !== window.location.origin) {
  // for dev environment
  axios.defaults.headers.common.Authorization = 'Basic dXNlcjpjaGFuZ2VtZQ==';
}

function loadAndThen(cb: any): void {
  setTimeout(cb, latency);
}

function get(url: string, success: any, error: any): void {
  loadAndThen(() =>
    axios
      .get(url)
      .then(success)
      .catch(error)
  );
}

function post(url: string, data: any, success: any, error: any): void {
  loadAndThen(() =>
    axios
      .post(url, data)
      .then(success)
      .catch(e => {
        console.error(e.response.data);
        error(e);
      })
  );
}

export function getConstants(success: any, error: any): void {
  get(constants, success, error);
}

export function getMods(success: any, error: any): void {
  get(mods, success, error);
}

export function getMod(slug: any, success: any, error: any): void {
  get(`${mods}/${slug}`, success, error);
}

export function getPack(slug: any, success: any, error: any): void {
  get(`${packs}/${slug}`, success, error);
}

export function getPacks(success: any, error: any): void {
  get(packs, success, error);
}

export function postMod(data: any, success: any, error: any): void {
  post(mods, data, success, error);
}

export function postModVersion(
  slug: any,
  data: any,
  success: any,
  error: any
): void {
  post(`${mods}/${slug}`, data, success, error);
}

export function postPack(data: any, success: any, error: any): void {
  post(packs, data, success, error);
}

export function postPackVersion(
  slug: any,
  data: any,
  success: any,
  error: any
): void {
  post(`${packs}/${slug}`, data, success, error);
}

// eslint-disable-next-line @typescript-eslint/no-unused-vars
function slugify(str: string): string {
  return str
    .toLowerCase()
    .replace(/[^\w\s-]/g, '') // remove non-word [a-z0-9_], non-whitespace, non-hyphen characters
    .replace(/[\s_-]+/g, '-') // swap any length of whitespace, underscore, hyphen characters with a single -
    .replace(/^-+|-+$/g, ''); //  remove leading, trailing -
  // function written by kganser https://gist.github.com/mathewbyrne/1280286#gistcomment-1716050
}
