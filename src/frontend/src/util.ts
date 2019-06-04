import axios, { AxiosError, AxiosResponse } from 'axios';
import { DynamicInput } from './model';

const baseUrl = `${window.location.protocol}//${window.location.hostname}:8081`;
const root = `${baseUrl}/braze/api/`;
const constants = `${root}constants`;
const mods = `${root}mods`;
const packs = `${root}packs`;
const latency = 1600; // artifical latency

type HttpSuccessHandler<T> = (response: AxiosResponse<T>) => void;
type HttpErrorHandler = (error: AxiosError) => void;

if (baseUrl !== window.location.origin) {
  // for dev environment
  axios.defaults.headers.common.Authorization = 'Basic dXNlcjpjaGFuZ2VtZQ==';
}

function loadAndThen(cb: Function): void {
  setTimeout(cb, latency);
}

function get<T>(
  url: string,
  success: HttpSuccessHandler<T>,
  error: HttpErrorHandler
): void {
  loadAndThen(() =>
    axios
      .get<T>(url)
      .then(success)
      .catch(error)
  );
}

function post<T, U>(
  url: string,
  data: U,
  success: HttpSuccessHandler<T>,
  error: HttpErrorHandler
): void {
  loadAndThen(() =>
    axios
      .post<T>(url, data)
      .then(success)
      .catch(error)
  );
}

export function getConstants(
  success: HttpSuccessHandler<Constants>,
  error: HttpErrorHandler
): void {
  get(constants, success, error);
}

export function getMods(
  success: HttpSuccessHandler<SimpleMod[]>,
  error: HttpErrorHandler
): void {
  get(mods, success, error);
}

export function getMod(
  slug: string,
  success: HttpSuccessHandler<Mod>,
  error: HttpErrorHandler
): void {
  get(`${mods}/${slug}`, success, error);
}

export function getPack(
  slug: string,
  success: HttpSuccessHandler<Pack>,
  error: HttpErrorHandler
): void {
  get(`${packs}/${slug}`, success, error);
}

export function getPacks(
  success: HttpSuccessHandler<SimplePack[]>,
  error: HttpErrorHandler
): void {
  get(packs, success, error);
}

export function postMod(
  data: SimpleMod,
  success: HttpSuccessHandler<Mod>,
  error: HttpErrorHandler
): void {
  post(mods, data, success, error);
}

export function postModVersion(
  slug: string,
  data: IndexedModVersion,
  success: HttpSuccessHandler<Mod>,
  error: HttpErrorHandler
): void {
  post(`${mods}/${slug}`, data, success, error);
}

export function postPack(
  data: SimplePack,
  success: HttpSuccessHandler<Pack>,
  error: HttpErrorHandler
): void {
  post(packs, data, success, error);
}

export function postPackVersion(
  slug: string,
  data: IndexedPackVersion,
  success: HttpSuccessHandler<Pack>,
  error: HttpErrorHandler
): void {
  post(`${packs}/${slug}`, data, success, error);
}

export function inputsToObject<
  T extends { [K in DynamicInput['key']]: string | null }
>(inputs: DynamicInput[]): T {
  return inputs.reduce((acc: Partial<T>, input) => {
    acc[input.key] = input.value;
    return acc;
  }, {}) as T;
}

// https://stackoverflow.com/a/46941824/2172566
export type Overwrite<T1, T2> = { [P in Exclude<keyof T1, keyof T2>]: T1[P] } &
  T2;

// eslint-disable-next-line @typescript-eslint/no-unused-vars
function slugify(str: string): string {
  return str
    .toLowerCase()
    .replace(/[^\w\s-]/g, '') // remove non-word [a-z0-9_], non-whitespace, non-hyphen characters
    .replace(/[\s_-]+/g, '-') // swap any length of whitespace, underscore, hyphen characters with a single -
    .replace(/^-+|-+$/g, ''); //  remove leading, trailing -
  // function written by kganser https://gist.github.com/mathewbyrne/1280286#gistcomment-1716050
}
