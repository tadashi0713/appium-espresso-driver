import chai from 'chai';
import chaiAsPromised from 'chai-as-promised';
import wd from 'wd';
import { HOST, PORT, MOCHA_TIMEOUT } from '../helpers/session';
import { APIDEMO_CAPS } from '../desired';
import { startServer } from '../../..';


chai.should();
chai.use(chaiAsPromised);

describe('elementByXPath', function () {
  this.timeout(MOCHA_TIMEOUT);

  let driver;
  let server;
  before(async () => {
    server = await startServer(PORT, HOST);
    driver = wd.promiseChainRemote(HOST, PORT);
  });
  after(async () => {
    try {
      await server.close();
    } catch (ign) {}
  });
  beforeEach(async () => {
    try {
      await driver.init(APIDEMO_CAPS);
    } catch (ign) {}
  });
  afterEach(async () => {
    try {
      await driver.quit();
    } catch (ign) {}
  });

  it('should find an element by it\'s xpath', async () => {
    let el = await driver.elementByXPath("//*[@content-desc='Animation']");
    el.should.exist;
    await el.click();
  });
  it('should find multiple elements that match one xpath', async () => {
    let els = await driver.elementsByXPath('//android.widget.TextView');
    els.length.should.be.above(1);
  });
  it('should get the first element of an xpath that matches more than one element', async () => {
    let el = await driver.elementByXPath('//android.widget.TextView');
    el.should.exist;
  });
  it('should throw a stale element exception if clicking on element that does not exist', async () => {
    let el = await driver.elementByXPath("//*[@content-desc='Animation']");
    await el.click();
    await el.click().should.eventually.be.rejectedWith(/no longer attached /);
  });
  it('should find elements by Hamcrest selector strategy', async () => {
    const viewsEl = await driver.elementByAccessibilityId("Views");
    await viewsEl.click();
    let el2 = await driver.element('-espresso hamcrest', JSON.stringify({
      hasEntry: {
        key: {
          equalTo: "contentDescription"
        },
        value: {
          equalTo: "Lists"
        }
      }
    }));
    await el2.click();
    let el3 = await driver.element('-espresso hamcrest', JSON.stringify({
      hasEntry: {
        key: {
          equalTo: "contentDescription"
        },
        value: {
          equalTo: "04. ListAdapter"
        }
      }
    }));
    await el3.click();
    let el4 = await driver.element('-espresso hamcrest', JSON.stringify({
      isA: "Integer",
      equalTo: 7,
    }));
  });
});
