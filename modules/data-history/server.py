import os
import yaml

from aiohttp import web
from pymongo import MongoClient


def load_config(fn: str):
    with open(fn, 'r') as f:
        return yaml.load(f, yaml.BaseLoader)


class Server:
    def __init__(self, bind_host: str, bind_port: int, conn_str: str, db: str, col: str):
        self.bind_port: int = bind_port
        self.bind_host: str = bind_host

        self.app: web.Application = web.Application()
        self.mc = MongoClient(conn_str)
        self.db = db
        self.col = col

    async def get(self, request):
        entity_field = request.match_info.get('entity_field', '')
        entity_id = request.match_info.get('entity_id', '')
        ts_gt = request.match_info.get('ts_gt', '')
        ts_lt = request.match_info.get('ts_lt', '')
        return web.json_response(
            data=list(self.mc[self.db][self.col].find(
                {
                    entity_field: entity_id,
                    'ts': {'$gt': int(ts_gt), '$lt': int(ts_lt)}
                }, {'_id': False}
            )))

    def run(self):

        self.app.router.add_route('GET', '/{entity_field}/{entity_id}/{ts_gt}-{ts_lt}', self.get)
        web.run_app(self.app, host=self.bind_host, port=self.bind_port)
        print('done')


if __name__ == '__main__':
    config_fn = os.getenv('CONFIG_FN', None)
    if not config_fn:
        raise RuntimeError('CONFIG_FN variable is not exist')

    config = load_config(config_fn)

    server = Server(
        conn_str=config['conn_str'],
        db=config['db'],
        col=config['col'],
        bind_host=config['bind_host'],
        bind_port=config['bind_port']
    )

    server.run()