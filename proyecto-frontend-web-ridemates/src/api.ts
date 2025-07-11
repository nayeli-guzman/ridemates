import axios, {
  AxiosRequestConfig,
  AxiosResponse,
  RawAxiosRequestHeaders,
} from "axios";

export default class Api {
  private static _instance: Api | null = null;
  private _basePath: string;
  private _authorization: string | null;

  private constructor(basePath: string, authorization: string | null) {
    this._basePath = basePath;
    this._authorization = authorization;
  }

  public set authorization(value: string) {
    this._authorization = value;
  }
  public static async getInstance() {
    if (!this._instance) {
      const basePath = `http://127.0.0.1:8081`;
      this._instance = new Api(basePath, null);
    }

    return this._instance;
  }

  public async request<RequestType, ResponseType>(options: AxiosRequestConfig) {
    const headers: RawAxiosRequestHeaders = {
      "Content-Type": "application/json",
      //"Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYWx2YWRyLmRvbmF5cmVAdXRlYy5lZHUucGUiLCJpYXQiOjE3MzIyNTI3OTksImV4cCI6MTczMjI1NjM5OX0.VclUzDELZfApnVanlqzvm1TvglZfxtsmjqqCYN8oWyA"

      Authorization: this._authorization ? `Bearer ${this._authorization}` : "",
    };

    const configOptions: AxiosRequestConfig = {
      ...options,
      baseURL: this._basePath,
      headers: headers,
    };

    const path = this._basePath + options.url;

    return axios<RequestType, AxiosResponse<ResponseType>>(path, configOptions);
  }

  public get<RequestBodyType, ResponseBodyType>(options: AxiosRequestConfig) {
    const configOptions: AxiosRequestConfig = {
      ...options,
      method: "GET",
    };

    return this.request<RequestBodyType, ResponseBodyType>(configOptions);
  }

  public post<RequestBodyType, ResponseBodyType>(
    data: RequestBodyType,
    options: AxiosRequestConfig,
  ) {
    const configOptions: AxiosRequestConfig = {
      ...options,
      method: "POST",
      data,
    };

    return this.request<RequestBodyType, ResponseBodyType>(configOptions);
  }

  public delete(options: AxiosRequestConfig) {
    const configOptions: AxiosRequestConfig = {
      ...options,
      method: "DELETE",
    };

    return this.request<void, void>(configOptions);
  }

  public put<RequestBodyType, ResponseBodyType>(
    data: RequestBodyType,
    options: AxiosRequestConfig,
  ) {
    const configOptions: AxiosRequestConfig = {
      ...options,
      method: "PUT",
      data: data,
    };

    return this.request<RequestBodyType, ResponseBodyType>(configOptions);
  }
}
